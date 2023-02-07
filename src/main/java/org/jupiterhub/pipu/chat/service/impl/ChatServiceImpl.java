package org.jupiterhub.pipu.chat.service.impl;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.repository.ChatRepository;
import org.jupiterhub.pipu.chat.service.ChatService;
import org.jupiterhub.pipu.chat.util.KeyGenUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class ChatServiceImpl implements ChatService {

    @Inject
    @Named(ChatRepository.FIRESTORE_IMPL)
    ChatRepository chatRepository;

    @Override
    public List<Chat> getAllChats(int offset, int limit) {
        return chatRepository.getAllChats(offset, limit);
    }

    @Override
    public Chat getChatById(String chatId) {
        return chatRepository.getChatsById(chatId);
    }

    @Override
    public List<Chat> getChatsByUserId(String userId) {
        return chatRepository.getChatsByUserId(userId);
    }

    @Override
    public Chat saveChat(Chat chat) {
        if (chat.id() == null) {
            chat = new Chat(KeyGenUtil.createChatId(chat.people()), chat.people(), chat.messages(),
                    Instant.now().toEpochMilli());
        }
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
    public void deleteChat(String chatId) {
        chatRepository.deleteChat(chatId);
    }
}
