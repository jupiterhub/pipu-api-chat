package org.jupiterhub.pipu.chat.exception;

import org.jupiterhub.pipu.chat.constant.MessageApiError;

public class MessageApiException extends RuntimeException {

    public MessageApiException(String message, MessageApiError code) {
        super("["+code+"] " + message);
    }
    public MessageApiException(String message, MessageApiError code, RuntimeException e) {
        super("["+code+"] " + message, e);
    }
}
