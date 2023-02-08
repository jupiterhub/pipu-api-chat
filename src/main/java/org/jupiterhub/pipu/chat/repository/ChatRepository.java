package org.jupiterhub.pipu.chat.repository;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;

import java.util.List;

public interface ChatRepository {
    static final String FIRESTORE_IMPL = "chatRepositoryFirestore";
    static final String CASSANDRA_IMPL = "chatRepositoryCassandra";

    List<Chat> getAllChats(int offset, int limit);
    Chat getChatsById(String userId);
    List<Chat> getChatsByUserId(String userId);
    Chat saveChat(Chat chat);
    void deleteChat(String chatId);

    Message saveMessage(String chatId, Message message);
    Message getMessage(String chatId, String messageId);
    Message updateMessage(String chatId, String messageId, String message);
    void deleteMessage(String chatId, String messageId);
    MessageTimestamp markSent(String chatId, String messageId);
    MessageTimestamp markDelivered(String chatId, String messageId);
    MessageTimestamp markRead(String chatId, String messageId);

    List<Message> getMessagesByOffset(String chatId, int offset, int limit);
}
