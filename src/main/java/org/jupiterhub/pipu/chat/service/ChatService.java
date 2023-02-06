package org.jupiterhub.pipu.chat.service;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;

import java.util.List;

public interface ChatService {

    List<Chat> getAllChats(int offset, int limit);
    List<Chat> getChatsById(String userId);
    List<Chat> getChatsByUserId(String userId);
    Chat saveChat(Chat chat);
    Chat updateChat(Chat chat);
    void deleteChat(String chatId);

    Message saveMessage(String chatId, Message message);
    Message updateMessage(String chatId, String messageId, String message);
    Message deleteMessage(String chatId, String messageId);
    MessageTimestamp markSent(String chatId, String messageId);
    MessageTimestamp markDelivered(String chatId, String messageId);
    MessageTimestamp markRead(String chatId, String messageId);


    List<Message> getMessagesByOffset(String chatId, int offset, int limit);
}
