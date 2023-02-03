package org.jupiterhub.pipu.chat.record;

import java.util.Collection;

public record Match(String matchId, Collection<Chat> chats) {
}
