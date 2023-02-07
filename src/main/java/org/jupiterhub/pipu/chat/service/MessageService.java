package org.jupiterhub.pipu.chat.service;

import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;

import java.util.List;

public interface MessageService {

    Message sendMessage(String chatId, Message message);
    Message updateMessage(String chatId, String messageId, String message);
    Message deleteMessage(String chatId, String messageId);
    MessageTimestamp markSent(String chatId, String messageId);
    MessageTimestamp markDelivered(String chatId, String messageId);
    MessageTimestamp markRead(String chatId, String messageId);


    List<Message> getMessagesByOffset(String chatId, int offset, int limit);
}
