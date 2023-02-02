package org.jupiterhub.pipu.chat.resource;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jupiterhub.pipu.chat.record.client.Directory;
import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.service.ChatService;
import org.jupiterhub.pipu.chat.socket.ChatSocket;

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
    public void send(Chat chat) {
        chatService.sendMessage(chat);
    }

    @GET
    public Multi<List<Chat>> allChat() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/{id}")
    public Uni<Chat> lookup(String id) {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/{id}")
    public void delete(String id) {
        throw new UnsupportedOperationException();
    }

    @PUT
    @Path("/{id}")
    public Uni<Chat> update(Chat chat) {
        throw new UnsupportedOperationException();
    }

}
