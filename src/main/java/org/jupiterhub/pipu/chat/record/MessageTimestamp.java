package org.jupiterhub.pipu.chat.record;

/**
 * timestamps on when the event occurred
 */
public record MessageTimestamp(long sent, Long delivered, Long read) {
    public static final String FIELD_SENT ="sent";
    public static final String FIELD_DELIVERED ="delivered";
    public static final String FIELD_READ ="read";
}
