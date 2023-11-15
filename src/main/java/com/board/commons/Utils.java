package com.board.commons;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.ResourceBundle;


//편의 기능 로직
@Component
@RequiredArgsConstructor
public class Utils {
    //정적인 자원을 생성
    private static ResourceBundle validationsBundle;
    private static ResourceBundle errorsBundle;

    private final HttpServletRequest request;
    private final HttpSession session;

      //정적인 자원 해제
    static {
        validationsBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");

    }
    //bundle 타입에 따라서 나오는 값이 바뀌게
    public static String getMessage(String code, String bundleType){
        bundleType = Objects.requireNonNullElse(bundleType,"validation");
        ResourceBundle bundle = bundleType.equals("error")?errorsBundle:validationsBundle;

        try{
            return bundle.getString(code);
        }catch (Exception e){
            return null;
        }
    }
    //현재 접속매개체가 모바일인지 PC인지 확인
    public boolean isMobile() {

        String device =(String)session.getAttribute("device");
        if(device !=null){
            return device.equals("mobile");
        }


        // 요청 헤더 User-Agent
        boolean isMobile = request.getHeader("User-Agent").matches(".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*");

        return isMobile;
    }
    public String tpl(String tplPath){
        return String.format("%s/"+tplPath, isMobile()?"mobile":"front");
    }

    //세션 초기화(로그인 초기화)
    public static void loginInit(HttpSession session){
        session.removeAttribute("email");
        session.removeAttribute("NotBlank_email");
        session.removeAttribute("NotBlank_password");
        session.removeAttribute("globalError");
    }

}
