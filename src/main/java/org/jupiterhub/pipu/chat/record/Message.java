package org.jupiterhub.pipu.chat.record;

import java.time.Instant;

public record Message(String id, String message, String from, String to, MessageTimestamp timestamp) {
    public static final String FIELD_ID ="id";
    public static final String FIELD_MESSAGE ="message";
    public static final String FIELD_FROM ="from";
    public static final String FIELD_TO ="to";
    public static final String FIELD_TIMESTAMP ="timestamp";

    public Message(String message, String from, String to) {
        this(null, message, from, to,
                new MessageTimestamp(Instant.now().toEpochMilli(), null, null));
    }
    public Message(String id, String message, String from, String to) {
        this(id, message, from, to,
                new MessageTimestamp(Instant.now().toEpochMilli(), null, null));
    }
}