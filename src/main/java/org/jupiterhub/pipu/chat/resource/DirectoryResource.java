package org.jupiterhub.pipu.chat.resource;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jupiterhub.pipu.chat.client.Directory;
import org.jupiterhub.pipu.chat.service.DirectoryService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

/**
 * Meant for testing connection to DirectoryService
 */
@Path("/directory")
public class DirectoryResource {

    @RestClient
    private DirectoryService directoryService;

    @GET
    @Path("/{key}")
    public CompletionStage<Response> lookup(String key) {
        return directoryService.lookup(key);
    }
    @DELETE
    @Path("/{key}")
    public void delete(String key) {
        directoryService.delete(key);
    }

    @PUT
    @Path("/{key}")
    @Consumes()
    public Directory update(String key, Directory directory) {
        return directoryService.update(key, directory);
    }

    @POST
    public CompletionStage<Directory> register(Directory directory) {
         return directoryService.register(directory);
    }
}
