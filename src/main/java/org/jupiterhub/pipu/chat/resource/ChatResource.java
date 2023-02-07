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
    ChatService chatService;

    @POST
    public Chat createChat(Chat chat) {
        return chatService.saveChat(chat);
    }

    @GET
    public List<Chat> getUserChats(@RestQuery("u") String username) {
        return chatService.getChatsByUserId(username);
    }

    @Path("/{chatId}")
    @GET
    public Chat getChat(String chatId) {
        return chatService.getChatById(chatId);
    }
    @Path("/{chatId}")
    @DELETE
    public void delete(String chatId) {
        chatService.deleteChat(chatId);
    }

}
