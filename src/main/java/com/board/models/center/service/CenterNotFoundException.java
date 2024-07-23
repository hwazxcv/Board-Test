package com.board.models.center.service;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;

public class CenterNotFoundException extends AlertBackException {
    public CenterNotFoundException() {
        super(Utils.getMessage("NotFound.center" , "errors") , HttpStatus.NOT_FOUND);
    }
}
