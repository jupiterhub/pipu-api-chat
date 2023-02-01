package org.jupiterhub.pipu.chat.service.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jupiterhub.pipu.chat.record.client.Directory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

@Path("/directory")
@RegisterRestClient(configKey = "pipu-chatdir-api")
public interface DirectoryRestService {

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
    CompletionStage<Void> delete(@PathParam("key") String key);
}
