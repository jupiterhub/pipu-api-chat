package org.jupiterhub.pipu.chat.constant;

public enum ChatApiErrorCode {
    REPOSITORY_GET_CHAT_BY_USERID("rgcbuid"),
    REPOSITORY_GET_CHAT_BY_USERID_INVALID_JSON("rgcbuid_ij"),
    REPOSITORY_GET_CHAT_BY_USERID_TIMEOUT("rgcbuid_to"),

    REPOSITORY_GET_CHAT_BY_ID("rgcbid"),
    REPOSITORY_GET_CHAT_BY_ID_INVALID_JSON("rgcbid_ij"),
    REPOSITORY_GET_CHAT_BY_ID_TIMEOUT("rgcbid_to"),
    ;

    String code;

    ChatApiErrorCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
