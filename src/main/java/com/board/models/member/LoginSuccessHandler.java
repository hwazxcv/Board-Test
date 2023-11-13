package com.board.models.member;

import com.board.commons.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Objects;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    //요청과 응답,주로 회원 정보가 담겨있다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();
        Utils.loginInit(session);
        //로그인 성공시 페이지 이동
        //요청 데이터 redirectURL값이 있으면 이동 없으면 메인 페이지(/)

        String redirectURL = Objects.requireNonNullElse(request.getParameter("redirectURL"),"/");
        response.sendRedirect(request.getContextPath()+redirectURL);
    }
}
