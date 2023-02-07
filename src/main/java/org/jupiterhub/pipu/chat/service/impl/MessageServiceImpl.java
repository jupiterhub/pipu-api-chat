package org.jupiterhub.pipu.chat.service.impl;

import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;
import org.jupiterhub.pipu.chat.repository.ChatRepository;
import org.jupiterhub.pipu.chat.service.MessageService;
import org.jupiterhub.pipu.chat.socket.MessageSocketSocketService;
import org.jupiterhub.pipu.chat.util.KeyGenUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MessageServiceImpl implements MessageService {
    @Inject
    ChatRepository chatRepository;

    @Inject
    MessageSocketSocketService messageSocketSocketService;

    @Override
    public Message sendMessage(String chatId, Message message) {
        Message createdMessage = new Message(KeyGenUtil.createMessageId(message), message.message(), message.from(), message.to());
        chatRepository.saveMessage(chatId, createdMessage);
        messageSocketSocketService.sendMessage(createdMessage);
        return createdMessage;
    }

    @Override
    public Message updateMessage(String chatId, String messageId, String message) {
        return chatRepository.updateMessage(chatId, messageId, message);
    }

    @Override
    public Message deleteMessage(String chatId, String messageId) {
        return chatRepository.deleteMessage(chatId, messageId);
    }

    @Override
    public MessageTimestamp markSent(String chatId, String messageId) {
        return chatRepository.markSent(chatId, messageId);
    }

    @Override
    public MessageTimestamp markDelivered(String chatId, String messageId) {
        return chatRepository.markDelivered(chatId, messageId);
    }

    @Override
    public MessageTimestamp markRead(String chatId, String messageId) {
        return chatRepository.markRead(chatId, messageId);
    }

    @Override
    public List<Message> getMessagesByOffset(String chatId, int offset, int limit) {
        return chatRepository.getMessagesByOffset(chatId, offset, limit);
    }
}
