package org.jupiterhub.pipu.chat.resource;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestQuery;
import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.Message;
import org.jupiterhub.pipu.chat.service.ChatService;
import org.jupiterhub.pipu.chat.socket.MessageSocketSocketService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

/**
 * Meant for testing connection to DirectoryService
 */
@Path("/chat")
public class ChatResource {

    @Inject
    MessageSocketSocketService messageSocketService;

    @Inject
    ChatService chatService;

    @POST
    public Chat createChat(Chat chat) {
        return chatService.saveChat(chat);
    }

    @GET
    public List<Chat> lookup(String chatId, @RestQuery("u") String username) {
        return chatService.getChatsByUserId(username);
    }

    @Path("/{chatId}")
    @POST
    public void send(Message message) {
        messageSocketService.sendMessage(message);
    }


    @DELETE
    @Path("/{id}")
    public void delete(String id) {
        throw new UnsupportedOperationException();
    }

    @PUT
    @Path("/{id}")
    public Uni<Message> update(Message message) {
        throw new UnsupportedOperationException();
    }

}
