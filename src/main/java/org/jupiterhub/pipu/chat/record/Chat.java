package org.jupiterhub.pipu.chat.record;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jupiterhub.pipu.chat.exception.ChatIdCreationException;
import org.jupiterhub.pipu.chat.util.KeyGenUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @param people people involved in this chat
 * @param messages list of messages between these people
 */
//@RegisterForReflection
public record Chat(String id, List<String> people, List<Message> messages, Long created) {
    public static final String FIELD_ID ="id";
    public static final String FIELD_PEOPLE ="people";
    public static final String FIELD_MESSAGES ="messages";
    public static final String FIELD_CREATED ="created";

    public Chat {
        if (people == null || people.size() < 2) {
            throw new IllegalArgumentException("At least 2 people must be provided to generate a chat");
        }
        // ensure it's immutable
        people = List.copyOf(people);

        if (created == null) {
            created = Instant.now().toEpochMilli();
        }

        if (messages == null) {
            messages = Collections.emptyList();
        }

        if (!messages.isEmpty()) {
            messages = List.copyOf(messages);
        }
    }
}
