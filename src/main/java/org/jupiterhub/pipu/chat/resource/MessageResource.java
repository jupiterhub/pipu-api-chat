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
        System.out.println("## SEND MESSAGE " + message);
        System.out.println("## SEND MESSAGE " + chatId);
        return messageService.sendMessage(chatId, message);
    }

    @GET
    public List<Message> getAllMessages(String chatId) {
        return chatService.getChatById(chatId).messages();
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(Message message) {
        throw new UnsupportedOperationException();
    }

    @PUT
    @Path("/{messageId}")
    public Message updateMessage(Message message) {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/{messageId}")
    public Message deleteMessage(Message message) {
        throw new UnsupportedOperationException();
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
