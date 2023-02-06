package org.jupiterhub.pipu.chat.repository.impl;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;
import org.jupiterhub.pipu.chat.repository.ChatRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@ApplicationScoped
@Named(ChatRepository.FIRESTORE_IMPL)
public class ChatRepositoryFirestore implements ChatRepository {

    @Inject
    Firestore firestore;

    @Override
    public List<Chat> getAllChats(int offset, int limit) {
        return null;
    }

    @Override
    public List<Chat> getChatsById(String userId) {
        return null;
    }

    @Override
    public List<Chat> getChatsWithUser(String userId) {
        return null;
    }

    @Override
    public Chat saveChat(Chat chat) {
        CollectionReference chats = firestore.collection("chats");
        chats.add(chat); // Future
        return chat;
    }

    @Override
    public void deleteChat(String chatId) {

    }

    @Override
    public Message saveMessage(String chatId, Message message) {
        return null;
    }

    @Override
    public Message updateMessage(String chatId, String messageId, String message) {
        return null;
    }

    @Override
    public Message deleteMessage(String chatId, String messageId) {
        return null;
    }

    @Override
    public MessageTimestamp markSent(String chatId, String messageId) {
        return null;
    }

    @Override
    public MessageTimestamp markDelivered(String chatId, String messageId) {
        return null;
    }

    @Override
    public MessageTimestamp markRead(String chatId, String messageId) {
        return null;
    }

    @Override
    public List<Message> getMessagesByOffset(String chatId, int offset, int limit) {
        return null;
    }
}
