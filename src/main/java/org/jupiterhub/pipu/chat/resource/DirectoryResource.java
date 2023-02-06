package org.jupiterhub.pipu.chat.resource;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jupiterhub.pipu.chat.record.client.Directory;
import org.jupiterhub.pipu.chat.service.client.DirectoryRestService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

/**
 * Meant for testing connection to DirectoryService
 */
@Path("/directory")
public class DirectoryResource {

    @RestClient
    DirectoryRestService directoryRestService;

    @GET
    @Path("/{key}")
    public CompletionStage<Response> lookup(String key) {
        return directoryRestService.lookup(key);
    }
    @DELETE
    @Path("/{key}")
    public void delete(String key) {
        directoryRestService.delete(key);
    }

    @PUT
    @Path("/{key}")
    @Consumes()
    public Directory update(String key, Directory directory) {
        return directoryRestService.update(key, directory);
    }

    @POST
    public CompletionStage<Directory> register(Directory directory) {
         return directoryRestService.register(directory);
    }
}
