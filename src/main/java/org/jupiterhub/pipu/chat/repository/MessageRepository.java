package org.jupiterhub.pipu.chat.repository;

import org.jupiterhub.pipu.chat.entity.Message;

import java.util.List;

public interface MessageRepository {
    String FIRESTORE_IMPL = "messageRepositoryFirestore";

    Message saveMessage(Message message);

    Message getMessageById(String chatId, String messageId);

    List<Message> getMessageByChatId(String chatId);

    void updateMessage(String messageId, String message);

    void deleteMessage(String messageId);

    void markSent(String messageId);

    void markDelivered(String messageId);

    void markRead(String messageId);

    List<Message> getMessagesByOffset(String chatId, long offset, int limit);

    List<Message> getMessageByUserId(String userId);
}
