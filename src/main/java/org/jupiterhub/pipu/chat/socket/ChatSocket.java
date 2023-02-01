package org.jupiterhub.pipu.chat.socket;

import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.client.Directory;
import org.jupiterhub.pipu.chat.service.client.DirectoryRestService;
import org.jupiterhub.pipu.chat.util.EnvironmentUtil;
import org.jupiterhub.pipu.chat.util.JsonChatUtil;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@ServerEndpoint(value = "/chat/ws/{username}")
@ApplicationScoped
public class ChatSocket {
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Inject
    Logger log;

    @RestClient
    private DirectoryRestService directoryRestService;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        String hostname = EnvironmentUtil.getHostname();
        System.out.println("## HOST NAME : "+ hostname);
        System.out.println("## USERNAME : "+  username);
        directoryRestService.register(new Directory(username , hostname));

        // TODO: DB Load top k previous messages (cached on frontend)
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        directoryRestService.delete(username);
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        directoryRestService.delete(username);
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    private Chat asyncParse(String message) {
        try {
            return JsonChatUtil.decode(message);
        } catch (DecodeException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("username") String username) {
        Chat chat = asyncParse(message);
        System.out.println("## PARSED CHAT " + chat);
        if (sessions.get(chat.to()) != null) {
            System.out.println("## Sending message");
            sessions.get(chat.to()).getAsyncRemote().sendText(chat.message());
        } else {
            System.out.println("## Looking up different host");
            // not in this host, lookup and then send message
            directoryRestService.lookup(chat.to()).thenAccept(sendMessage(chat));
        }

        // also send message to self
        sessions.get(username).getAsyncRemote().sendText(chat.message());
    }

    private Consumer<Response> sendMessage(Chat chat) {
        return response -> {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                System.out.println("## User " + chat.to() + " does not exist");
                return;
            }

            Directory directory = response.readEntity(Directory.class);
            System.out.println("### RETRIEVED " + directory);

            System.out.println("## TODO: send message to a different host");
            // REST CLIENT -> SVC -> POD.
            // REST CLIENT NEEDS TO BE CONFIGURED TO THE SPECIFIC POD
        };
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message);
        });
    }
}
