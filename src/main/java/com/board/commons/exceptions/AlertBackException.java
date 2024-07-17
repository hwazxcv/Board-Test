package com.board.commons.exceptions;


import org.springframework.http.HttpStatus;

//예외처리하고 뒤로 돌아가는 로직
public class AlertBackException extends AlertException {

    public AlertBackException(String message , HttpStatus status) {
        super(message,status);
    }
}