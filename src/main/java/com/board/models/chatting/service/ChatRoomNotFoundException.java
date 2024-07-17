package com.board.models.chatting.service;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class ChatRoomNotFoundException extends AlertBackException {


    public ChatRoomNotFoundException() {
        super(Utils.getMessage("NotFound.chatroom") , HttpStatus.NOT_FOUND);
    }
}
