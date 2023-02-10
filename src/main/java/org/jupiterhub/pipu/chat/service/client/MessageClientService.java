package org.jupiterhub.pipu.chat.service.client;

import org.jupiterhub.pipu.chat.entity.Message;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.concurrent.CompletionStage;

@Path("/message")
public interface MessageClientService {

    @POST
    CompletionStage<Void> send(Message message);
}
