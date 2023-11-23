package com.board.models.board.config;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends AlertBackException {


    public BoardNotFoundException() {
        super(Utils.getMessage("NotFound.board", "errors"));
        setStatus(HttpStatus.NOT_FOUND);
    }
}
