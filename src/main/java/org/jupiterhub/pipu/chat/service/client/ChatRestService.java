package org.jupiterhub.pipu.chat.service.client;

import org.jupiterhub.pipu.chat.record.Message;

import javax.ws.rs.*;
import java.util.concurrent.CompletionStage;

@Path("/chat")
public interface ChatRestService {

    @POST
    CompletionStage<Void> send(String chatId, Message message);
}
