package com.board.commons;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;


//편의 기능 로직
@Component
@RequiredArgsConstructor
public class Utils {
    //정적인 자원을 생성
    private static ResourceBundle validationsBundle;

    private static ResourceBundle errorsBundle;

    private static ResourceBundle commonsBundle;

    private final HttpServletRequest request;

    private final HttpSession session;

      //정적인 자원 해제
    static {
          validationsBundle = ResourceBundle.getBundle("messages.validations");
          errorsBundle = ResourceBundle.getBundle("messages.errors");
          commonsBundle = ResourceBundle.getBundle("messages.commons");

    }
    //bundle 타입에 따라서 나오는 값이 바뀌게
    public static String getMessage(String code, String bundleType){
        bundleType = Objects.requireNonNullElse(bundleType,"validation");
        ResourceBundle bundle = null;

        if (bundleType.equals("commons")) {
            bundle = commonsBundle;
        } else if (bundleType.equals("errors")) {
            bundle = errorsBundle;
        } else {
            bundle = validationsBundle;
        }

        try{
            return bundle.getString(code);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static String getMessage(String code) {
        return getMessage(code, null);
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
    //장비에 따라서 모바일인지 PC인지
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

    /**
     * 목록 데이터 처리를 위한
     *
     * 단일 요청 데이터 조회
     */
    public String getParam(String name) {
        return request.getParameter(name);
    }

    /**
     * 복수개 요청 데이터 조회
     *
     */
    public String[] getParams(String name) {
        return request.getParameterValues(name);
    }


    //페이징 처리
    //페이지 값이 0이하 처리
    public static int getNumber(int num, int defaultValue) {
        return num <= 0 ? defaultValue : num;
    }

    /**
     * 비회원 구분 UID
     * 비회원 구분은 IP + 브라우저 종류
     *
     */
    public int guestUid() {
        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");

        return Objects.hash(ip, ua);
    }
    public static Map<String, List<String>> getMessages(Errors errors) {
        try {
            Map<String, List<String>> data = new HashMap<>();
            for (FieldError error : errors.getFieldErrors()) {
                String field = error.getField();
                List<String> messages = Arrays.stream(error.getCodes()).sorted(Comparator.reverseOrder())
                        .map(c -> getMessage(c, "validation"))
                        .filter(c -> c != null)
                        .toList();

                data.put(field, messages);
            }

            return data;

        } catch (Exception e) {
            return null;
        }
    }




    public String nl2br(String str){
        //줄개행 문자를 바꿈
        return str.replaceAll("\\r","").replaceAll("\\n" ,"<br>");
    }




}