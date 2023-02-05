package org.jupiterhub.pipu.chat.resource;

import org.jupiterhub.pipu.chat.record.Message;

public interface ChatRepository {
    Message save(Message message);
}
