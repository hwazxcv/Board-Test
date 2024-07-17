package com.board.commons;


import com.board.commons.exceptions.AlertBackException;
import com.board.commons.exceptions.AlertException;
import com.board.commons.exceptions.AlertRedirectException;
import com.board.commons.exceptions.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface ExceptionProcessor {



    @ExceptionHandler(Exception.class)
    default String errorHandler(Exception e , HttpServletResponse response, HttpServletRequest request, Model model) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 기본 값 500 에러
        if (e instanceof CommonException) {
            CommonException commonException = (CommonException) e;
            status = commonException.getStatus();
        }

        response.setStatus(status.value());

        e.printStackTrace();

        String script = null;
        if (e instanceof AlertException) { // 자바스크립트 Alert형태로 응답

            script = String.format("alert('%s');", e.getMessage());
            if (e instanceof AlertBackException) {
                script += "history.back()";
            }
            if(e instanceof AlertRedirectException){
                AlertRedirectException alertRedirectException = (AlertRedirectException) e;
                script += String.format("%s.location.replace('%s')",alertRedirectException.getTarget(),alertRedirectException.getRedirectUrl());
            }
        }


        model.addAttribute("script", script );
        return "common/_execute_script";
    }


}
