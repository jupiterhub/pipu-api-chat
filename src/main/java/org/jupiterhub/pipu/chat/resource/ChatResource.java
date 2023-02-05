package org.jupiterhub.pipu.chat.resource;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jupiterhub.pipu.chat.record.Message;
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

    @POST
    public void send(Message message) {
        messageSocketService.sendMessage(message);
    }

    @GET
    public Multi<List<Message>> allChat() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/{id}")
    public Uni<Message> lookup(String id) {
        throw new UnsupportedOperationException();
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
