package com.board.controllers.members;

import com.board.commons.CommonProcess;
import com.board.commons.Utils;
import com.board.models.member.MemberSaveService;
import com.board.models.member.dtos.RequestJoin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements CommonProcess {


    private final Utils utils;

    private final MemberSaveService saveService;

    //모바일과 front를 연결하는 컨트롤러
    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form,Model model){
        commonProcess(model,Utils.getMessage("회원가입" , "common"));

        return utils.tpl("member/join");
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form , Errors errors,Model model){

        commonProcess(model,Utils.getMessage("회원가입" , "common"));
        saveService.join(form,errors);

        //error에 에러가 담겨있으면 로그인 회원가입 페이지
        if(errors.hasErrors()){
            return utils.tpl("member/join");
        }
        //통과하면 로그인 페이지
        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(String redirectURL , Model model){
        commonProcess(model,Utils.getMessage("로그인" , "common"));
        model.addAttribute("redirectURL" , redirectURL);
       return utils.tpl("member/login");
    }



}
//    @GetMapping("/info")
//    @ResponseBody
//    public void info(){
//
//        Member member = memberUtil.getMember();
//        if(memberUtil.isLogin()){
//        log.info(member.toString());
//        }
//        log.info("로그인 여부: {}", memberUtil.isLogin());

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

