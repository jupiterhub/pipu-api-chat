package org.jupiterhub.pipu.chat.service;

import org.jupiterhub.pipu.chat.entity.NewMessage;

import java.util.List;

public interface MessageService {

    NewMessage getMessageById(String chatId, String messageId);

    List<NewMessage> getMessageByChatId(String chatId);

    NewMessage sendMessage(String chatId, NewMessage message);

    void updateMessage(String messageId, String message);

    void deleteMessage(String chatId, String messageId);

    void markSent(String chatId, String messageId);

    void markDelivered(String chatId, String messageId);

    void markRead(String chatId, String messageId);


    List<NewMessage> getMessagesByOffset(String chatId, long offset, int limit);

    List<NewMessage> getMessageByUserId(String userId);
}
