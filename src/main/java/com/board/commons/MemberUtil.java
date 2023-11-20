package com.board.commons;

import com.board.entities.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUtil {


    private final HttpSession session;

    //로그인 상태 체크 중
    public boolean isLogin(){

        return getMember() !=null;
    }


    public Member getMember(){
        return (Member)session.getAttribute("loginMember");
    }


}
