package org.jupiterhub.pipu.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class MessageRequest {

    @NotBlank(message = "from is required")
    private String from;
    @NotBlank(message = "to is required")
    private String to;
    @NotBlank(message = "message is required")
    private String message;

}