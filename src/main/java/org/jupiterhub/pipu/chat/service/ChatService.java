package org.jupiterhub.pipu.chat.service;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;

import java.util.List;

public interface ChatService {

    List<Chat> getAllChats(int offset, int limit);
    List<Chat> getChatsById(String userId);
    List<Chat> getChatsWithUser(String userId);
    Chat saveChat(Chat chat);
    Chat updateChat(Chat chat);
    void deleteChat(int chatId);

    Message saveMessage(int chatId, Message message);
    Message updateMessage(int chatId, String messageId, String message);
    Message deleteMessage(int chatId, String messageId);
    MessageTimestamp markSent(int chatId, String messageId);
    MessageTimestamp markDelivered(int chatId, String messageId);
    MessageTimestamp markRead(int chatId, String messageId);


    List<Message> getMessagesByOffset(String chatId, int offset, int limit);
}
