package org.jupiterhub.pipu.chat.socket;

import org.jupiterhub.pipu.chat.entity.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class MessageSocketSocketService implements IMessageSocketService {
    private final Map<String, Session> activeSessions = new ConcurrentHashMap<>();

    @Override
    public void openSession(String username, Session session) {
        // only store to session if there are no exceptions
        // TODO: DB Load top k previous messages (cached on frontend)
        activeSessions.put(username, session);
    }

    @Override
    public void closeSession(String username) {
        Session remove = activeSessions.remove(username);

        try {
            remove.close();
        } catch (IOException ignore) {
            
        }
    }

    @Override
    public void sendMessage(Message message) {
        sendMessage(message.getTo(), message.getMessage());
    }

    @Override
    public void sendMessage(String to, String message) {
        Session session = activeSessions.get(to);
        if (session != null) {
            session.getAsyncRemote().sendText(message);
        }
    }

    @Override
    public boolean isActive(String username) {
        return activeSessions.containsKey(username);
    }

    @Override
    public Map<String, Session> getActiveSessions() {
        return activeSessions;
    }

}
