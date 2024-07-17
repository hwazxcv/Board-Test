package com.board.commons.exceptions;

import org.springframework.http.HttpStatus;


//공통 예외 처리 클래스

public class CommonException extends RuntimeException{
    

    private HttpStatus status;
    
    
    public CommonException(String message){
        this(message, HttpStatus.INTERNAL_SERVER_ERROR); //기본적으로 나오는 에러 (500)
    }
    
    public CommonException(String message, HttpStatus status){
        super(message);
        this.status=status;
    }

    public HttpStatus getStatus() {
        return status;
    }
    
    
}
