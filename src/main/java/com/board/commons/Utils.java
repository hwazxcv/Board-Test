package com.board.commons;


import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class Utils {
    //정적인 자원을 생성
    private static ResourceBundle validationsBundle;
    private static ResourceBundle errorsBundle;

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

}
