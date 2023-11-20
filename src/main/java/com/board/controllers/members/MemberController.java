package com.board.controllers.members;

import com.board.commons.MemberUtil;
import com.board.commons.Utils;
import com.board.commons.constants.MemberType;
import com.board.entities.BoardData;
import com.board.entities.Member;
import com.board.repositories.BoardDataRepository;
import com.board.repositories.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Transactional
public class MemberController {


    private final Utils utils;
    private final MemberUtil memberUtil;
    private final EntityManager em;

    @Autowired
    private BoardDataRepository boardDataRepository;

    @Autowired
    private MemberRepository memberRepository;

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
        Member member = Member.builder()
                .email("user01@test.org")
                .password("123456")
                .userNm("사용자01")
                .mtype(MemberType.USER)
                .build();
        memberRepository.saveAndFlush(member);

        List<BoardData> items = new ArrayList<>();
        for(int i =1; i<=10; i++){

        BoardData item = BoardData.builder()
                .subject("제목")
                .content("내용")
                .member(member)
                .build();
        items.add(item);
        }
        boardDataRepository.saveAllAndFlush(items);

        BoardData data = boardDataRepository.findById(1L).orElse(null);

        Member member2 = data.getMember();
        String email = member2.getEmail(); // 2차 쿼리 실행
        System.out.println(email);


//        Member member = memberUtil.getMember();
//        if(memberUtil.isLogin()){
//        log.info(member.toString());
//        }
//        log.info("로그인 여부: {}", memberUtil.isLogin());

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
