package com.board.models.board;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class BoardDataNotFoundException extends AlertBackException {
    public BoardDataNotFoundException() {
        super(Utils.getMessage("NotFound.boardData" , "error"));
        setStatus(HttpStatus.NOT_FOUND);
    }
}
