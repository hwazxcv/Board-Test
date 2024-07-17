package com.board.commons;

import com.board.commons.constants.MemberType;
import com.board.entities.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//전용 서비스 사용을 위한 클래스

@Component
@RequiredArgsConstructor
public class MemberUtil {


    private final HttpSession session;

    //로그인 상태(여부) 체크 중
    public boolean isLogin(){

        return getMember() !=null;
    }

    /**
     * 관리자 여부 체크
     * @return
     */
    public boolean isAdmin() {
        return isLogin() && getMember().getMtype() == MemberType.ADMIN;
    }




    public Member getMember(){
        return (Member)session.getAttribute("loginMember");
    }


    public static void clearLoginData(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("NotBlank_username");
        session.removeAttribute("NotBlank_password");
        session.removeAttribute("Global_error");
    }


}
