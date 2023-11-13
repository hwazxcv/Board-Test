package com.board.controllers.members;

import com.board.commons.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {


    private final Utils utils;

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
}
