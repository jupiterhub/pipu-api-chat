package org.jupiterhub.pipu.chat.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import io.quarkus.logging.Log;
import org.jupiterhub.pipu.chat.constant.ChatApiErrorCode;
import org.jupiterhub.pipu.chat.exception.ChatRepositoryException;
import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.record.MessageTimestamp;
import org.jupiterhub.pipu.chat.repository.ChatRepository;
import org.jupiterhub.pipu.chat.util.JsonChatUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ApplicationScoped
@Named(ChatRepository.FIRESTORE_IMPL)
public class ChatRepositoryFirestore implements ChatRepository {

    public static final String COLLECTION_NAME = "chats";
    @Inject
    Firestore firestore;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public List<Chat> getAllChats(int offset, int limit) {
        return null;
    }

    @Override
    public List<Chat> getChatsById(String userId) {
        return null;
    }

    @Override
    public List<Chat> getChatsByUserId(String userId) {
        CollectionReference chatsRef = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> querySnapshot = chatsRef.whereArrayContains(Chat.FIELD_PEOPLE, userId).get();

        try {
            return querySnapshot.get().getDocuments().stream()
                    .map(document -> {
                        try {
                            return JsonChatUtil.decodeChat(
                                    objectMapper.writeValueAsString(document.getData()));
                        } catch (JsonProcessingException e) {
                            throw new ChatRepositoryException("@getChatsByUserId. Unable to parse json from Firestore. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_USERID_INVALID_JSON);
                        }
                    })
                    .toList();
        } catch (InterruptedException e) {
            throw new ChatRepositoryException("Interrupted while getting chats. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_USERID);
        } catch (ExecutionException e) {
            throw new ChatRepositoryException("Execution failed while getting chats. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_USERID);
        }
    }

    /**
     * Java records doesn't conform to POJO so it's unable to find it
     * Manually map, as this allows us to do without reflections too
     */
    @Override
    public Chat saveChat(Chat chat) {
        CollectionReference chats = firestore.collection(ChatRepositoryFirestore.COLLECTION_NAME);
        chats.document(chat.id()).create(
                Map.of(
                Chat.FIELD_ID, chat.id(),
                Chat.FIELD_MESSAGES, chat.messages(),
                Chat.FIELD_PEOPLE, chat.people(),
                Chat.FIELD_CREATED, chat.created()
                )); // Future
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
