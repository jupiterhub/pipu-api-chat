package org.jupiterhub.pipu.chat.record;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jupiterhub.pipu.chat.exception.ChatIdCreationException;
import org.jupiterhub.pipu.chat.util.KeyGenUtil;

import java.util.List;

/**
 * @param people people involved in this chat
 * @param messages list of messages between these people
 */
//@RegisterForReflection
public record Chat(String id, List<String> people, List<Message> messages) {
    public Chat {
        if (people == null || people.size() < 2) {
            throw new IllegalArgumentException("At least 2 people must be provided to generate a chat");
        }
        // ensure it's immutable
        people = List.copyOf(people);

        if (messages != null && !messages.isEmpty()) {
            messages = List.copyOf(messages);
        }
    }
}
