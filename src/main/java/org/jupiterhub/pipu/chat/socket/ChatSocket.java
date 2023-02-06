package org.jupiterhub.pipu.chat.socket;

import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.client.Directory;
import org.jupiterhub.pipu.chat.service.client.ChatRestService;
import org.jupiterhub.pipu.chat.service.client.DirectoryRestService;
import org.jupiterhub.pipu.chat.util.EnvironmentUtil;
import org.jupiterhub.pipu.chat.util.JsonChatUtil;
import org.jupiterhub.pipu.chat.util.KeyGenUtil;

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
    MessageSocketSocketService messageSocketService;

    @ConfigProperty(name = "pipu.chat-api.uri")
    String uri;

    @RestClient
    DirectoryRestService directoryRestService;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        System.out.println("## URI INJECTED: " + uri);
        String replacedUri = uri.replace("{POD_NAME}", EnvironmentUtil.getHostname());
        System.out.println("## HOST NAME : " + replacedUri);
        System.out.println("## USERNAME : " + username);

        Directory directory = new Directory(username, replacedUri);
        directoryRestService.register(directory)
                .thenAccept(unused -> {
                    messageSocketService.openSession(username, session);
                })
                .exceptionally(throwable -> {   // ORDER IS IMPORTANT. Exceptionally after thenAccept to ensure it doesn't run
                    Log.errorf("@onOpen. Failed to call directory service. Not registering %s. Cause: %s", directory, throwable.getMessage());
                    return null;
                });


    }

    @OnClose
    public void onClose(@PathParam("username") String username) {
        directoryRestService.delete(username);
        messageSocketService.closeSession(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        directoryRestService.delete(username);
        messageSocketService.closeSession(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    private Message parseJson(String message) {
        try {
            return JsonChatUtil.decode(message);
        } catch (DecodeException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        Message chatThread = parseJson(message);
        if (messageSocketService.isActive(chatThread.to())) {
            Log.info("Sending message locally");
            messageSocketService.sendMessage(chatThread.to(), chatThread.message());
            messageSocketService.sendMessage(chatThread.from(), chatThread.message());
        } else {
            Log.info("Sending message remotely");
            // not in this host, lookup and then send message
            directoryRestService.lookup(chatThread.to())
                    .thenAccept(sendRemoteMessage(chatThread))
                    .thenAccept(unused -> messageSocketService.sendMessage(chatThread.from(), chatThread.message()))   // send to self too
                    .exceptionally(throwable -> {
                        Log.errorf("@onMessage. Failed to call directory service. Not sending %s. Cause: %s", chatThread, throwable.getMessage());
                        return null;
                    });
        }

    }


    private Consumer<Response> sendRemoteMessage(Message message) {
        return response -> {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                Log.infof("User %s not found in remote, not sending message.", message.to());
                return;
            }

            Directory directory = response.readEntity(Directory.class);
            ChatRestService build = RestClientBuilder.newBuilder()
                    .baseUri(URI.create(directory.host()))
                    .build(ChatRestService.class);

            build.send(KeyGenUtil.commutativeKey(message.from(), message.to()).toString(), message).exceptionally(throwable -> {
                Log.errorf("@onMessage. Failed to send remote[%s] message %s. Cause: %s", directory.host(), message, throwable.getMessage());
                return null;
            });
        };
    }

    private void broadcast(String message) {
        messageSocketService.getActiveSessions().values().forEach(s -> {
            s.getAsyncRemote().sendObject(message);
        });
    }
}
