package org.jupiterhub.pipu.chat.service.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jupiterhub.pipu.chat.record.Chat;
import org.jupiterhub.pipu.chat.record.client.Directory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

@Path("/chat")
public interface ChatRestService {

    @POST
    CompletionStage<Void> send(Chat chat);
}
