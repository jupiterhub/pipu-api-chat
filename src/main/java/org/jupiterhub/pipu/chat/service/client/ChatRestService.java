package org.jupiterhub.pipu.chat.service.client;

import org.jboss.resteasy.reactive.RestPath;
import org.jupiterhub.pipu.chat.record.Message;

import javax.ws.rs.*;
import java.util.concurrent.CompletionStage;

@Path("/chat/{chatId}")
public interface ChatRestService {

    @POST
    CompletionStage<Void> send(@RestPath String chatId, Message message);
}
