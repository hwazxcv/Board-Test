package com.board.models.board;

import com.board.commons.Utils;
import com.board.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class RequiredPasswordCheckException extends CommonException {
    public RequiredPasswordCheckException() {
        super(Utils.getMessage("Required.guestPw.check", "validation"), HttpStatus.UNAUTHORIZED);
    }
}
