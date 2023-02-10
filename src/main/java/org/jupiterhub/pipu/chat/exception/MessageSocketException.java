package org.jupiterhub.pipu.chat.exception;

import org.jupiterhub.pipu.chat.constant.MessageSocketError;

public class MessageSocketException extends RuntimeException {
    public MessageSocketException(String s, MessageSocketError code) {
        super("[" + code + "] " + s);
    }
}
