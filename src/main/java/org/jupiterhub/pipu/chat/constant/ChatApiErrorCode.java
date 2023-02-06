package org.jupiterhub.pipu.chat.constant;

public enum ChatApiErrorCode {
    REPOSITORY_GET_CHAT_BY_USERID("rgcbuid"),
    REPOSITORY_GET_CHAT_BY_USERID_INVALID_JSON("rgcbuidij");

    String code;

    ChatApiErrorCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
