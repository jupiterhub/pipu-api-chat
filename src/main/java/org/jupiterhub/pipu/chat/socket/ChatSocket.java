package org.jupiterhub.pipu.chat.socket;

import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.client.Directory;
import org.jupiterhub.pipu.chat.service.ChatService;
import org.jupiterhub.pipu.chat.service.client.ChatRestService;
import org.jupiterhub.pipu.chat.service.client.DirectoryRestService;
import org.jupiterhub.pipu.chat.util.EnvironmentUtil;
import org.jupiterhub.pipu.chat.util.JsonChatUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.function.Consumer;

@ServerEndpoint(value = "/chat/ws/{username}")
@ApplicationScoped
public class ChatSocket {

    @Inject
    private ChatService chatService;

    @ConfigProperty(name = "pipu.chat-api.uri")
    private String uri;

    @RestClient
    private DirectoryRestService directoryRestService;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        System.out.println("## URI INJECTED: " + uri);
        String replacedUri = uri.replace("{POD_NAME}", EnvironmentUtil.getHostname());
        System.out.println("## HOST NAME : " + replacedUri);
        System.out.println("## USERNAME : " + username);

        Directory directory = new Directory(username, replacedUri);
        directoryRestService.register(directory)
                .thenAccept(unused -> {
                    chatService.openSession(username, session);
                })
                .exceptionally(throwable -> {   // ORDER IS IMPORTANT. Exceptionally after thenAccept to ensure it doesn't run
                    Log.errorf("@onOpen. Failed to call directory service. Not registering %s. Cause: %s", directory, throwable.getMessage());
                    return null;
                });


    }

    @OnClose
    public void onClose(@PathParam("username") String username) {
        directoryRestService.delete(username);
        chatService.closeSession(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        directoryRestService.delete(username);
        chatService.closeSession(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    private Chat parseJson(String message) {
        try {
            return JsonChatUtil.decode(message);
        } catch (DecodeException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        Chat chat = parseJson(message);
        if (chatService.isActive(chat.to())) {
            Log.info("Sending message locally");
            chatService.sendMessage(chat.to(), chat.message());
            chatService.sendMessage(chat.from(), chat.message());
        } else {
            Log.info("Sending message remotely");
            // not in this host, lookup and then send message
            directoryRestService.lookup(chat.to())
                    .thenAccept(sendRemoteMessage(chat))
                    .thenAccept(unused -> chatService.sendMessage(chat.from(), chat.message()))   // send to self too
                    .exceptionally(throwable -> {
                        Log.errorf("@onMessage. Failed to call directory service. Not sending %s. Cause: %s", chat, throwable.getMessage());
                        return null;
                    });
        }

    }


    private Consumer<Response> sendRemoteMessage(Chat chat) {
        return response -> {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                Log.infof("User %s not found in remote, not sending message.", chat.to());
                return;
            }

            Directory directory = response.readEntity(Directory.class);
            ChatRestService build = RestClientBuilder.newBuilder()
                    .baseUri(URI.create(directory.host()))
                    .build(ChatRestService.class);

            build.send(chat).exceptionally(throwable -> {
                Log.errorf("@onMessage. Failed to send remote[%s] message %s. Cause: %s", directory.host(), chat, throwable.getMessage());
                return null;
            });
        };
    }

    private void broadcast(String message) {
        chatService.getActiveSessions().values().forEach(s -> {
            s.getAsyncRemote().sendObject(message);
        });
    }
}
