package com.board.models.member;

import com.board.commons.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    //요청과 응답 실패시 오류를 담고있다.
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        HttpSession session = request.getSession();

        //세션값 초기화 - Utils에 만들어져있음
        Utils.loginInit(session);

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //값이참이면 검증 완료 아니면 실패
        boolean isRequiredFiledCheck = false;

        session.setAttribute("email",email);


        //필수 항목 검증 -- email , password
        if(email == null || email.isBlank()){
            session.setAttribute("NotBlank_email" , Utils.getMessage("NotBlank.email","validation"));
            isRequiredFiledCheck = true;
        }
        if(password == null || password.isBlank()){
            session.setAttribute("NotBlank_password" , Utils.getMessage("NotBlank.password","validation"));
            isRequiredFiledCheck = true;
        }

        // 아이디가 없거나 비밀번호가 잘못된 경우
        if(!isRequiredFiledCheck){
            session.setAttribute("globalError" , Utils.getMessage("Login.fail","validation"));
        }

        //로그인 실패시 항상 로그인페이지 이동
        response.sendRedirect(request.getContextPath()+"/member/login");
    }


}
