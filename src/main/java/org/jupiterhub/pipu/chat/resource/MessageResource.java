package org.jupiterhub.pipu.chat.resource;

import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.service.ChatService;
import org.jupiterhub.pipu.chat.service.MessageService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/message/{chatId}")
public class MessageResource {

    @Inject
    MessageService messageService;
    @Inject
    ChatService chatService;

    @POST
    public Message create(String chatId, Message message) {
        return messageService.sendMessage(chatId, message);
    }

    @GET
    public List<Message> getAllMessages(String chatId) {
        return chatService.getChatById(chatId).messages();
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(String chatId, String messageId) {
        return messageService.getMessage(chatId, messageId);
    }

    @PUT
    @Path("/{messageId}")
    public Message updateMessage(String chatId, String messageId, Message message) {
        return messageService.updateMessage(chatId, messageId, message.message());
    }

    @DELETE
    @Path("/{messageId}")
    public void deleteMessage(String chatId, String messageId) {
        messageService.deleteMessage(chatId, messageId);
    }

    @PUT
    @Path("/{messageId}/sent")
    public void markSent(String chatId, String messageId) {
        messageService.markSent(chatId, messageId);
    }

    @PUT
    @Path("/{messageId}/delivered")
    public void markDelivered(String chatId, String messageId) {
        messageService.markDelivered(chatId, messageId);
    }

    @PUT
    @Path("/{messageId}/read")
    public void markRead(String chatId, String messageId) {
        messageService.markRead(chatId, messageId);
    }
}
