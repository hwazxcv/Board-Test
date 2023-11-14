package com.board.controllers.members;

import com.board.commons.MemberUtil;
import com.board.commons.Utils;
import com.board.entities.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {


    private final Utils utils;
    private final MemberUtil memberUtil;

    //모바일과 front를 연결하는 컨트롤러
    @GetMapping("/join")
    public String join(){
        return utils.tpl("member/join");
    }

    @GetMapping("/login")
    public String login(String redirectURL , Model model){
        model.addAttribute("redirectURL" , redirectURL);
       return utils.tpl("member/login");
    }


    @GetMapping("/info")
    @ResponseBody
    public void info(){

        Member member = memberUtil.getMember();
        if(memberUtil.isLogin()){
        log.info(member.toString());
        }
        log.info("로그인 여부: {}", memberUtil.isLogin());

    }
//      Principal 주입 방법 3가지
//    public void info(){
//        MemberInfo member=(MemberInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        log.info(member.toString());
//    }
//    public void info(@AuthenticationPrincipal MemberInfo memberInfo){
//        log.info(memberInfo.toString());
//    }

//    public void info(Principal principal){
//        String email = principal.getName();
//        log.info(email);
//    }
}
