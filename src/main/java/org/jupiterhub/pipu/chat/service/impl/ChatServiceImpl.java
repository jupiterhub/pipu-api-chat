package org.jupiterhub.pipu.chat.service.impl;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;
import org.jupiterhub.pipu.chat.repository.ChatRepository;
import org.jupiterhub.pipu.chat.service.ChatService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@ApplicationScoped
public class ChatServiceImpl implements ChatService {

    @Inject
    @Named("firebaseChatRepository")
    private ChatRepository chatRepository;

    @Override
    public List<Chat> getAllChats(int offset, int limit) {
        return chatRepository.getAllChats(offset, limit);
    }

    @Override
    public List<Chat> getChatsById(String chatId) {
        return chatRepository.getChatsById(chatId);
    }

    @Override
    public List<Chat> getChatsWithUser(String userId) {
        return chatRepository.getChatsWithUser(userId);
    }

    @Override
    public Chat saveChat(Chat chat) {
        return chatRepository.saveChat(chat);
    }

    /**
     * shorthand for {@link #saveChat(Chat)}
     */
    @Override
    public Chat updateChat(Chat chat) {
        return saveChat(chat);
    }

    @Override
    public void deleteChat(int chatId) {
        chatRepository.deleteChat(chatId);
    }

    @Override
    public Message saveMessage(int chatId, Message message) {
        return chatRepository.saveMessage(chatId, message);
    }

    @Override
    public Message updateMessage(int chatId, String messageId, String message) {
        return chatRepository.updateMessage(chatId, messageId, message);
    }

    @Override
    public Message deleteMessage(int chatId, String messageId) {
        return chatRepository.deleteMessage(chatId, messageId);
    }

    @Override
    public MessageTimestamp markSent(int chatId, String messageId) {
        return chatRepository.markSent(chatId, messageId);
    }

    @Override
    public MessageTimestamp markDelivered(int chatId, String messageId) {
        return chatRepository.markDelivered(chatId, messageId);
    }

    @Override
    public MessageTimestamp markRead(int chatId, String messageId) {
        return chatRepository.markRead(chatId, messageId);
    }

    @Override
    public List<Message> getMessagesByOffset(String chatId, int offset, int limit) {
        return chatRepository.getMessagesByOffset(chatId, offset, limit);
    }
}
