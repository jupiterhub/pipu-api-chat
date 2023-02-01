package org.jupiterhub.pipu.chat.record;

public record Chat(String id, String message, String from, String to, Long timestamp) {
}
