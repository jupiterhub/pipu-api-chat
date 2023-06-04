package org.jupiterhub.pipu.chat.vo;

import javax.validation.constraints.NotBlank;

public class MessageRequest {

    @NotBlank(message = "from is required")
    private String from;
    @NotBlank(message = "to is required")
    private String to;
    @NotBlank(message = "message is required")
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}