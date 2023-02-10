package org.jupiterhub.pipu.chat.constant;

public enum MessageApiError {
    GET_ALL_MSG_CONTROLLER_MISSING_USER_ID("C_MA_1000"),
    GET_MSG_BY_MSG_ID_REPO("R_MA_2000"),
    GET_MSG_BY_CHAT_ID_REPO("R_MA_2001"),
    GET_MSG_BY_OFFSET_REPO("R_MA_2002"),
    GET_MSG_BY_USER_ID_REPO("R_MA_2003"),
    ;

    final String code;

    MessageApiError(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
