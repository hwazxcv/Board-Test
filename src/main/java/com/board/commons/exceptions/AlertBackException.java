package com.board.commons.exceptions;


//예외처리하고 뒤로 돌아가는 로직
public class AlertBackException extends AlertException {

    public AlertBackException(String message) {
        super(message);
    }
}