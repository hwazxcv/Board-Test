package com.board.models.board.config;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;


// 에외 발생 ( Alert로 오류 보여주고 뒤로 넘어가기)
public class BoardNotFoundException extends AlertBackException {


    public BoardNotFoundException() {
        super(Utils.getMessage("NotFound.board", "errors"),HttpStatus.BAD_REQUEST);
    }
}
