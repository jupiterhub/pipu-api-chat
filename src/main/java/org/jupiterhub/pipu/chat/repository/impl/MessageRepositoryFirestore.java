package org.jupiterhub.pipu.chat.repository.impl;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import org.jupiterhub.pipu.chat.constant.MessageApiError;
import org.jupiterhub.pipu.chat.entity.Message;
import org.jupiterhub.pipu.chat.exception.MessageApiException;
import org.jupiterhub.pipu.chat.repository.MessageRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
@Named(MessageRepository.FIRESTORE_IMPL)
public class MessageRepositoryFirestore implements MessageRepository {
    public static final String MSG_COLLECTION = "messages";
    public static final int MAX_QUERY_TIME_IN_SEC = 5;

    @Inject
    Firestore firestore;

    @Override
    public Message saveMessage(Message message) {
        firestore.collection(MSG_COLLECTION).document(message.getMessageId()).create(message);
        return message;
    }

    @Override
    public Message getMessageById(String chatId, String messageId) {
        try {
            return firestore.collection(MSG_COLLECTION).document(messageId).get()
                    .get(MAX_QUERY_TIME_IN_SEC, TimeUnit.SECONDS).toObject(Message.class);
        } catch (InterruptedException e) {
            throw new MessageApiException("Interrupted while getting message. " + e.getMessage(), MessageApiError.GET_MSG_BY_MSG_ID_REPO);
        } catch (ExecutionException e) {
            throw new MessageApiException("Execution failed while getting message. " + e.getMessage(), MessageApiError.GET_MSG_BY_MSG_ID_REPO);
        } catch (TimeoutException e) {
            throw new MessageApiException("Timeout. 5 seconds passed to get message. " + e.getMessage(), MessageApiError.GET_MSG_BY_MSG_ID_REPO);
        }
    }

    @Override
    public List<Message> getMessageByChatId(String chatId) {
        try {
            return firestore.collection(MSG_COLLECTION)
                    .whereEqualTo("chatId", chatId)
                    .orderBy("sentTimestamp", Query.Direction.ASCENDING)
                    .get().get(MAX_QUERY_TIME_IN_SEC, TimeUnit.SECONDS)
                    .getDocuments().stream().map(snapshot -> snapshot.toObject(Message.class))
                    .toList();
        } catch (InterruptedException e) {
            throw new MessageApiException("Interrupted while getting message. " + e.getMessage(), MessageApiError.GET_MSG_BY_CHAT_ID_REPO);
        } catch (ExecutionException e) {
            throw new MessageApiException("Execution failed while getting message. " + e.getMessage(), MessageApiError.GET_MSG_BY_CHAT_ID_REPO);
        } catch (TimeoutException e) {
            throw new MessageApiException("Timeout. 5 seconds passed to get message. " + e.getMessage(), MessageApiError.GET_MSG_BY_CHAT_ID_REPO);
        }
    }

    @Override
    public void updateMessage(String messageId, String message) {
        firestore.collection(MSG_COLLECTION).document(messageId).update("message", message);
    }

    @Override
    public void deleteMessage(String messageId) {
        firestore.collection(MSG_COLLECTION).document(messageId).delete();
    }

    @Override
    public void markSent(String messageId) {
        firestore.collection(MSG_COLLECTION).document(messageId).update("sentTimestamp", Instant.now().toEpochMilli());
    }

    @Override
    public void markDelivered(String messageId) {
        firestore.collection(MSG_COLLECTION).document(messageId).update("deliveredTimestamp", Instant.now().toEpochMilli());
    }

    @Override
    public void markRead(String messageId) {
        firestore.collection(MSG_COLLECTION).document(messageId).update("readTimestamp", Instant.now().toEpochMilli());
    }

    @Override
    public List<Message> getMessagesByOffset(String chatId, long offset, int limit) {
        try {
            return firestore.collection(MSG_COLLECTION)
                    .whereEqualTo("chatId", chatId)
                    .orderBy("sentTimestamp", Query.Direction.DESCENDING)   // get last n records
                    .startAfter(offset)
                    .limit(limit)
                    .get().get(MAX_QUERY_TIME_IN_SEC, TimeUnit.SECONDS).getDocuments().stream()
                    .map(snap -> snap.toObject(Message.class))
                    .sorted(Comparator.comparing(Message::getSentTimestamp))     // reverse order again so we can get the last n-limit..n
                    .toList();
        } catch (InterruptedException e) {
            throw new MessageApiException("Interrupted while getting message. " + e.getMessage(), MessageApiError.GET_MSG_BY_OFFSET_REPO);
        } catch (ExecutionException e) {
            throw new MessageApiException("Execution failed while getting message. " + e.getMessage(), MessageApiError.GET_MSG_BY_OFFSET_REPO);
        } catch (TimeoutException e) {
            throw new MessageApiException("Timeout. 5 seconds passed to get message. " + e.getMessage(), MessageApiError.GET_MSG_BY_OFFSET_REPO);
        }
    }

    @Override
    public List<Message> getMessageByUserId(String userId) {
        try {
            return firestore.collection(MSG_COLLECTION)
                    .whereArrayContains("people", userId)
                    .orderBy("sentTimestamp", Query.Direction.ASCENDING)   // get last n records
                    .get().get(MAX_QUERY_TIME_IN_SEC, TimeUnit.SECONDS).getDocuments().stream()
                    .map(snap -> snap.toObject(Message.class))
                    .toList();
        } catch (InterruptedException e) {
            throw new MessageApiException("Interrupted while getting message. " + e.getMessage(), MessageApiError.GET_MSG_BY_USER_ID_REPO);
        } catch (ExecutionException e) {
            throw new MessageApiException("Execution failed while getting message. " + e.getMessage(), MessageApiError.GET_MSG_BY_USER_ID_REPO);
        } catch (TimeoutException e) {
            throw new MessageApiException("Timeout. 5 seconds passed to get message. " + e.getMessage(), MessageApiError.GET_MSG_BY_USER_ID_REPO);
        }
    }
}
