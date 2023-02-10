package org.jupiterhub.pipu.chat.constant;

public enum MessageSocketError {
    ON_MESSAGE_JSON_INVALID("S_MS_1000"),
    ;

    final String code;

    MessageSocketError(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
