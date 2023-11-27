package com.board.controllers.boards;

import com.board.commons.MemberUtil;
import com.board.commons.ScriptExceptionProcess;
import com.board.commons.Utils;
import com.board.entities.BoardData;
import com.board.models.board.BoardInfoService;
import com.board.models.board.BoardSaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
//                                      게시판 아이디가없으면 스크립트로 뒤로 넘기기
public class BoardController implements ScriptExceptionProcess {

    private final Utils utils;

    private final MemberUtil memberUtil;
    private final BoardSaveService saveService;
    private final BoardInfoService infoService;


    @GetMapping("/write/{bId}")
    public String write(@PathVariable String bId, @ModelAttribute BoardForm form, Model model){ // 게시판 쓰기
        commonProcess(bId,"write", model);

        return utils.tpl("board/write"); //모바일 혹은 PC연동
    }


    @GetMapping("/update/{seq}")
    public String update(@PathVariable Long seq , Model model){ // 게시판 수정
        return utils.tpl("board/update");
    }

    @PostMapping("/save")
    public String save(@Valid BoardForm form, Errors errors, Model model){ //게시물저장 -> 이후에 페이지 이동
        String mode = Objects.requireNonNullElse(form.getMode(),"write");
        String bId = form.getBId();
        commonProcess(bId, mode ,model);
        if(errors.hasErrors()){
            return utils.tpl("board/"+mode);
        }
        saveService.save(form);
        return "redirect:/board/list"+bId;
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable Long seq, Model model){

        BoardData data = infoService.get(seq);
        model.addAttribute("boardData",data);
        return utils.tpl("board/view");
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable Long seq){ //게시판 삭제 -> 이후에 페이지 이동

        return "redirect:/board/list/게시판ID";
    }


    //페이지마다 변경될 공통 속성
    private void commonProcess( String bId , String mode, Model model ){
        String pageTitle="게시글 목록";
        if(mode.equals("write")) pageTitle="게시글 작성";
        else if (mode.equals("update")) pageTitle="게시글 수정";
        else if (mode.equals("view")) pageTitle="게시글 제목";

        List<String> addCommonScript = new ArrayList<>();
        List<String > addScript = new ArrayList<>();

        if(mode.equals("write") || mode.equals("update")){
            addCommonScript.add("ckeditor/ckeditor");
            addCommonScript.add("fileManager");

            addScript.add("board/form");
        }
        model.addAttribute("addCommonScript" , addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("pageTitle" , pageTitle);
    }


}
