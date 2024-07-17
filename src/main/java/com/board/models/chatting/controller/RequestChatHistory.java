package com.board.models.chatting.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestChatHistory {

    @NotBlank
    private String roomId;

    @NotBlank
    private String nicName;

    @NotBlank
    private String message;

}
