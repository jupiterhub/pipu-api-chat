package org.jupiterhub.pipu.chat.socket;

import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jupiterhub.pipu.chat.client.Directory;
import org.jupiterhub.pipu.chat.service.DirectoryService;
import org.jupiterhub.pipu.chat.util.EnvironmentUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/ws/{username}")
@ApplicationScoped
public class ChatSocket {
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @RestClient
    private DirectoryService directoryService;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        String hostname = EnvironmentUtil.getHostname();
        System.out.println("## HOST NAME : "+ hostname);
        directoryService.register(new Directory(username, hostname));
        // TODO: DB Load top k previous messages (cached on frontend)
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        directoryService.delete(username);
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        directoryService.delete(username);
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("username") String username) {

        System.out.println("## Sending message to with this session: " + session);
        System.out.println("## Sending message to with this session (from session map): " + sessions.get(username));
        directoryService.lookup(username).thenApply(response -> {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                System.out.println("## User " + username + " does not exist");
            }
            Directory directory = response.readEntity(Directory.class);
            System.out.println("### RETRIEVED " + directory);

            if (EnvironmentUtil.isSameHost(directory.host())) {
                // target is connected to same host, we can just send message directly
                sessions.get(username).getAsyncRemote().sendText(message);
            } else {
                System.out.println("## TODO: send message to a different host");
                // REST CLIENT -> SVC -> POD.
                // REST CLIENT NEEDS TO BE CONFIGURED TO THE SPECIFIC POD
            }

            // OLD method
//            if (message.equalsIgnoreCase("_ready_")) {
//                broadcast("User " + username + " joined");
//            } else {
//                broadcast(">> " + username + ": " + message);
//            }
            return directory;
        });
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message);
        });
    }
}
