package org.jupiterhub.pipu.chat.exception;

import org.jupiterhub.pipu.chat.constant.ChatApiErrorCode;

public class ChatRepositoryException extends RuntimeException {

    public ChatRepositoryException(String message, ChatApiErrorCode code) {
        super("["+code+"] " + message);
    }
    public ChatRepositoryException(String message, ChatApiErrorCode code, RuntimeException e) {
        super("["+code+"] " + message, e);
    }
}
