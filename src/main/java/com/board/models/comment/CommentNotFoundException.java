package com.board.models.comment;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertBackException;
import org.springframework.http.HttpStatus;


public class CommentNotFoundException extends AlertBackException {
    public CommentNotFoundException() {
        super(Utils.getMessage("NotFound.comment" , "error"),HttpStatus.BAD_REQUEST);
    }
}
