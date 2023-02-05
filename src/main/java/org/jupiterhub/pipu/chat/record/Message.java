package org.jupiterhub.pipu.chat.record;

public record Message(String id, String message, String from, String to, Long timestamp) {
}