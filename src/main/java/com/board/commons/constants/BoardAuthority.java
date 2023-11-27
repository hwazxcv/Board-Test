package com.board.commons.constants;

import java.util.Arrays;
import java.util.List;

public enum BoardAuthority {

    ALL("비회원 + 회원 + 관리자"),
    MEMBER("회원 + 관리자"),
    ADMIN("관리자");

    //정적 상수 객체
    private final String title;
    //생성자
    BoardAuthority(String title){
        this.title = title;
    }

    //정적 매서드
    public static List<String []> getList(){
        return Arrays.asList(

                new String[] { ALL.name(), ALL.title},
                new String [] {MEMBER.name(), MEMBER.title},
                new String [] {ADMIN.name() , ADMIN.title}
        );
    }
    //getter
    public String getTitle(){
        return title;
    }


}
