package org.jupiterhub.pipu.chat.record;

/**
 * timestamps on when the event occurred
 */
public record MessageTimestamp(long sent, Long delivered, Long read) {
}
