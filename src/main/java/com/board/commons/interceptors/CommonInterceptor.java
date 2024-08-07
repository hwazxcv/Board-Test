package com.board.commons.interceptors;

import com.board.commons.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


//공통적인 인터셉터
@Component
public class CommonInterceptor implements HandlerInterceptor {

    //장비에 대한 초기화작업
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        init(request);
        return true;
    }

    private void init(HttpServletRequest request){
        HttpSession session = request.getSession();

        //PC , Mobile 수동 변경 처리 기능
        //모바일인지 pc인지 세션에 담아서 처리
        String device = request.getParameter("device");
        
        if(device != null && !device.isBlank()){
            session.setAttribute("device",device.toLowerCase().equals("mobile")?"mobile":"pc");
        }
        //로그인 페이지 아닐 경우 로그인 유효성 검사 세션 삭제 처리
        String URI = request.getRequestURI();
        //-1이면 로그인 페이지가 아님 (세션 삭제)
        if(URI.indexOf("/member/login") == -1){
            Utils.loginInit(session);
        }


    }
}
