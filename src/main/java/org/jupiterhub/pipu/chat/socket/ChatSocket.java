package org.jupiterhub.pipu.chat.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jupiterhub.pipu.chat.constant.MessageSocketError;
import org.jupiterhub.pipu.chat.entity.Message;
import org.jupiterhub.pipu.chat.exception.MessageSocketException;
import org.jupiterhub.pipu.chat.record.client.Directory;
import org.jupiterhub.pipu.chat.service.MessageService;
import org.jupiterhub.pipu.chat.service.client.DirectoryRestService;
import org.jupiterhub.pipu.chat.service.client.MessageClientService;
import org.jupiterhub.pipu.chat.util.EnvironmentUtil;

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

    @Inject
    ObjectMapper objectMapper;

    @ConfigProperty(name = "pipu.chat-api.uri")
    String uri;

    @RestClient
    DirectoryRestService directoryRestService;

    @Inject
    MessageService messageService;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        System.out.println("## URI INJECTED: " + uri);
        String replacedUri = uri.replace("{POD_NAME}", EnvironmentUtil.getHostname());
        System.out.println("## HOST NAME : " + replacedUri);
        System.out.println("## USERNAME : " + username);

        Directory directory = new Directory(username, replacedUri);
        directoryRestService.register(directory)
                .thenAccept(unused -> messageSocketService.openSession(username, session))
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

    @OnMessage
    public void onMessage(String jsonMessage) {
        Message message = getMessage(jsonMessage);
        if (messageSocketService.isActive(message.getTo())) {
            Log.info("Sending message locally");
            messageService.sendMessage(message);
            messageSocketService.sendMessage(message.getFrom(), message.getMessage());  // also send to self
        } else {
            Log.info("Sending message remotely");
            // not in this host, lookup and then send message
            directoryRestService.lookup(message.getTo())
                    .thenAccept(sendRemoteMessage(message))
                    .thenAccept(unused -> messageSocketService.sendMessage(message.getFrom(), message.getMessageId()))   // send to self too
                    .exceptionally(e -> {
                        // target is not online, client should handle if there's an error
                        Log.errorf("@onMessage. Failed to call directory service. Not sending %s.\nCause: %s", message, e.getMessage());
                        return null;
                    });
        }

    }

    private Message getMessage(String jsonMessage) {
        Message message;
        try {
            message = objectMapper.readValue(jsonMessage, Message.class);
        } catch (JsonProcessingException e) {
            throw new MessageSocketException("Invalid Json. unable to send message. " + e.getMessage(), MessageSocketError.ON_MESSAGE_JSON_INVALID);
        }
        return message;
    }


    private Consumer<Response> sendRemoteMessage(Message message) {
        return response -> {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                Log.infof("User %s not found in remote, not sending message.", message.getTo());
                return;
            }

            Directory directory = response.readEntity(Directory.class);
            MessageClientService build = RestClientBuilder.newBuilder()
                    .baseUri(URI.create(directory.host()))
                    .build(MessageClientService.class);

            build.send(message).exceptionally(throwable -> {
                Log.errorf("@onMessage. Failed to send remote[%s] message %s. Cause: %s", directory.host(), message, throwable.getMessage());
                return null;
            });
        };
    }

    private void broadcast(String message) {
        messageSocketService.getActiveSessions().values()
                .forEach(s -> s.getAsyncRemote().sendObject(message));
    }
}
