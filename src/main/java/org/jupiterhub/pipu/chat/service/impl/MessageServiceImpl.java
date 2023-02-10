package org.jupiterhub.pipu.chat.service.impl;

import org.jupiterhub.pipu.chat.entity.Message;
import org.jupiterhub.pipu.chat.repository.MessageRepository;
import org.jupiterhub.pipu.chat.service.MessageService;
import org.jupiterhub.pipu.chat.socket.MessageSocketSocketService;
import org.jupiterhub.pipu.chat.util.KeyGenUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class MessageServiceImpl implements MessageService {
    @Inject
    MessageRepository messageRepository;

    @Inject
    MessageSocketSocketService messageSocketSocketService;

    @Override
    public Message getMessageById(String chatId, String messageId) {
        return messageRepository.getMessageById(null, messageId);
    }

    @Override
    public List<Message> getMessageByChatId(String chatId) {
        return messageRepository.getMessageByChatId(chatId);
    }

    @Override
    public Message sendMessage(String chatId, Message message) {
        // set Id
        message.setPeople(List.of(message.getFrom(), message.getTo()));
        message.setChatId(KeyGenUtil.createChatId(message.getPeople()));
        message.setMessageId(KeyGenUtil.createMessageId(message.getFrom(), message.getTo()));
        message.setSentTimestamp(Instant.now().toEpochMilli());
        messageRepository.saveMessage(message);
//        messageSocketSocketService.sendMessage(message);  // TODO

        return message;
    }

    @Override
    public void updateMessage(String messageId, String message) {
        messageRepository.updateMessage(messageId, message);
    }

    @Override
    public void deleteMessage(String chatId, String messageId) {
        messageRepository.deleteMessage(null, messageId);
    }

    @Override
    public void markSent(String chatId, String messageId) {
        messageRepository.markSent(chatId, messageId);
    }

    @Override
    public void markDelivered(String chatId, String messageId) {
        messageRepository.markDelivered(chatId, messageId);
    }

    @Override
    public void markRead(String chatId, String messageId) {
        messageRepository.markRead(chatId, messageId);
    }

    @Override
    public List<Message> getMessagesByOffset(String chatId, long offset, int limit) {
        return messageRepository.getMessagesByOffset(chatId, offset, limit);
    }

    @Override
    public List<Message> getMessageByUserId(String userId) {
        return messageRepository.getMessageByUserId(userId);
    }
}
