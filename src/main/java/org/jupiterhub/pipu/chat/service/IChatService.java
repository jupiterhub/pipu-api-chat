package org.jupiterhub.pipu.chat.service;

import org.jupiterhub.pipu.chat.record.Chat;

import javax.websocket.Session;
import java.util.Map;

public interface IChatService {
    void openSession(String username, Session session);
    void closeSession(String username);
    void sendMessage(Chat chat);
    void sendMessage(String to, String message);

    boolean isActive(String username);

    Map<String, Session> getActiveSessions();
}
