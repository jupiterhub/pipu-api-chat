package org.jupiterhub.pipu.chat.record;

import java.time.Instant;

public record Message(String id, String message, String from, String to, MessageTimestamp timestamp) {
    public Message(String id, String message, String from, String to) {
        this(id, message, from, to,
                new MessageTimestamp(Instant.now().toEpochMilli(), null, null));
    }
}