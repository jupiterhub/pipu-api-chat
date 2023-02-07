package org.jupiterhub.pipu.chat.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
@Named(ChatRepository.FIRESTORE_IMPL)
public class ChatRepositoryFirestore implements ChatRepository {

    public static final String CHATS_COLLECTION = "chats";
    @Inject
    Firestore firestore;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public List<Chat> getAllChats(int offset, int limit) {
        return null;
    }

    @Override
    public Chat getChatsById(String chatId) {
        try {
            return convertToChatObject(firestore.collection(CHATS_COLLECTION).document(chatId).get().get(5, TimeUnit.SECONDS).getData(),
                    "Unable to parse json from Firestore. ",
                    ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_ID_INVALID_JSON);
        } catch (InterruptedException e) {
            throw new ChatRepositoryException("Interrupted while getting chats. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_ID);
        } catch (ExecutionException e) {
            throw new ChatRepositoryException("Execution failed while getting chats. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_ID);
        } catch (TimeoutException e) {
            throw new ChatRepositoryException("Timeout. 5 seconds passed to get chats. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_ID_TIMEOUT);
        }
    }

    private Chat convertToChatObject(Map<String, Object> data, String jsonParseExceptionMessage, ChatApiErrorCode jsonParseExceptionCode) {
        try {
            return JsonChatUtil.decodeChat(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new ChatRepositoryException(jsonParseExceptionMessage + e.getMessage(), jsonParseExceptionCode);
        }
    }

    @Override
    public List<Chat> getChatsByUserId(String userId) {
        CollectionReference chatsRef = firestore.collection(CHATS_COLLECTION);
        ApiFuture<QuerySnapshot> querySnapshot = chatsRef.whereArrayContains(Chat.FIELD_PEOPLE, userId).get();

        try {
            return querySnapshot.get(5, TimeUnit.SECONDS).getDocuments().stream()
                    .map(document -> convertToChatObject(
                            document.getData(), "Unable to parse json from Firestore. ",
                            ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_USERID_INVALID_JSON))
                    .toList();
        } catch (InterruptedException e) {
            throw new ChatRepositoryException("Interrupted while getting chats. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_USERID);
        } catch (ExecutionException e) {
            throw new ChatRepositoryException("Execution failed while getting chats. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_USERID);
        } catch (TimeoutException e) {
            throw new ChatRepositoryException("Timeout. 5 seconds passed to get chats. " + e.getMessage(), ChatApiErrorCode.REPOSITORY_GET_CHAT_BY_USERID_TIMEOUT);
        }
    }

    /**
     * Java records doesn't conform to POJO so it's unable to find it
     * Manually map, as this allows us to do without reflections too
     */
    @Override
    public Chat saveChat(Chat chat) {
        CollectionReference chats = firestore.collection(ChatRepositoryFirestore.CHATS_COLLECTION);
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
        firestore.collection(CHATS_COLLECTION).document(chatId).delete();
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
