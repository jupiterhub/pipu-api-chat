package org.jupiterhub.pipu.chat.record;

import java.util.ArrayList;
import java.util.List;

/**
 * @param people people involved in this chat
 * @param messages list of messages between these people
 */
public record Chat(int id, List<String> people, List<Message> messages) {
    public Chat {
        // ensure it's immutable
        people = List.copyOf(people);
        messages = List.copyOf(messages);
    }
}
