package org.jupiterhub.pipu.chat.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jupiterhub.pipu.chat.client.Directory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

@Path("/directory")
@RegisterRestClient(configKey = "pipu-chatdir-api")
public interface DirectoryService {

    @POST
    CompletionStage<Directory> register(Directory directory);

    @GET
    @Path("/{key}")
    CompletionStage<Response> lookup(@PathParam("key") String key);

    @PUT
    @Path("/{key}")
    Directory update(@PathParam("key") String key, Directory directory);

    @DELETE
    @Path("/{key}")
    void delete(@PathParam("key") String key);
}
