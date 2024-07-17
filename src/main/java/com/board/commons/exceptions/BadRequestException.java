package com.board.commons.exceptions;

import com.board.commons.Utils;
import org.springframework.http.HttpStatus;

public class BadRequestException extends AlertBackException {
    public BadRequestException(String message , HttpStatus status) {
        super(message , status);
    }

    public BadRequestException() {
        super(Utils.getMessage("BadRequest", "error") , HttpStatus.BAD_REQUEST);
    }
}