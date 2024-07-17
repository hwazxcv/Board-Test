package com.board.restcontrollers;

import com.board.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends AlertBackException {


    public MemberNotFoundException() {
        super("등록된 회원이 아닙니다." , HttpStatus.BAD_REQUEST);
    }
}
