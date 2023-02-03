package org.jupiterhub.pipu.chat.resource;

import org.jupiterhub.pipu.chat.record.Chat;

public interface ChatRepository {
    Chat save(Chat chat);
}
