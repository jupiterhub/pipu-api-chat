package org.jupiterhub.pipu.chat.resource;

import org.jboss.resteasy.reactive.RestQuery;
import org.jupiterhub.pipu.chat.constant.MessageApiError;
import org.jupiterhub.pipu.chat.entity.Message;
import org.jupiterhub.pipu.chat.exception.MessageApiException;
import org.jupiterhub.pipu.chat.service.MessageService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.time.Instant;
import java.util.List;

@Path("/message")
public class MessageResource {

    @Inject
    MessageService messageService;


    @Path("/byChat/{chatId}")
    @GET
    public List<Message> getAllMessages(String chatId, @RestQuery Long offset, @RestQuery Integer limit) {
        if (limit != null) {
            offset = offset != null ? offset : Instant.now().toEpochMilli();
            return messageService.getMessagesByOffset(chatId, offset, limit);
        } else {
            return messageService.getMessageByChatId(chatId);
        }
    }

    @GET
    public List<Message> getAllMessages(@RestQuery String userId) {

        if (userId == null) {
            throw new MessageApiException("`userId` must be provided as a queryParameter to get messages. Getting all messages is not supported.", MessageApiError.GET_ALL_MSG_CONTROLLER_MISSING_USER_ID);
        }

        return messageService.getMessageByUserId(userId);
    }

    @POST
    public Message create(Message message) {
        return messageService.sendMessage(null, message);    // chatId is generated from message
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(String messageId) {
        return messageService.getMessageById(null, messageId);
    }

    @PUT
    @Path("/{messageId}")
    public void updateMessage(String messageId, String message) {
        messageService.updateMessage(messageId, message);
    }

    @DELETE
    @Path("/{messageId}")
    public void deleteMessage(String chatId, String messageId) {
        messageService.deleteMessage(chatId, messageId);
    }

    @PUT
    @Path("/{messageId}/sent")
    public void markSent(String messageId) {
        messageService.markSent(null, messageId);
    }

    @PUT
    @Path("/{messageId}/delivered")
    public void markDelivered(String messageId) {
        messageService.markDelivered(null, messageId);
    }

    @PUT
    @Path("/{messageId}/read")
    public void markRead(String messageId) {
        messageService.markRead(null, messageId);
    }
}
