package org.jupiterhub.pipu.chat.socket;

import org.jupiterhub.pipu.chat.entity.NewMessage;

import javax.websocket.Session;
import java.util.Map;

public interface IMessageSocketService {
    void openSession(String username, Session session);

    void closeSession(String username);

    void sendMessage(NewMessage message);

    void sendMessage(String to, String message);

    boolean isActive(String username);

    Map<String, Session> getActiveSessions();
}
