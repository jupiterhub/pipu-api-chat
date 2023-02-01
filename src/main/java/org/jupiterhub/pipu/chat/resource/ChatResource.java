package org.jupiterhub.pipu.chat.resource;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jupiterhub.pipu.chat.record.client.Directory;
import org.jupiterhub.pipu.chat.record.Chat;

import javax.ws.rs.*;
import java.util.List;

/**
 * Meant for testing connection to DirectoryService
 */
@Path("/chat")
public class ChatResource {


    @GET
    public Multi<List<Chat>> allChat() {
        return null;
    }

    @GET
    @Path("/{id}")
    public Uni<Chat> lookup(String id) {
        return null;
    }

    @DELETE
    @Path("/{id}")
    public void delete(String id) {

    }

    @PUT
    @Path("/{id}")
    public Uni<Chat> update(Chat chat) {
        return null;
    }

    @POST
    public Uni<Directory> register(Directory directory) {
        return null;
    }
}
