package org.jupiterhub.pipu.chat.record;

public record Chat(String chatId, String message, String from, String to, Long timestamp) {
}
