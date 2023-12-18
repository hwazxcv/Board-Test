package com.board.controllers.comments;

import com.board.commons.ScriptExceptionProcess;
import com.board.commons.Utils;
import com.board.commons.exceptions.AlertException;
import com.board.entities.BoardData;
import com.board.models.board.RequiredPasswordCheckException;
import com.board.models.comment.CommentDeleteService;
import com.board.models.comment.CommentInfoService;
import com.board.models.comment.CommentSaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//댓글 컨트롤러

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController implements ScriptExceptionProcess {

    private final CommentSaveService saveService;
    private final CommentDeleteService commentDeleteService;
    private final CommentInfoService infoService;
    private final Utils utils;

    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq , Model model){
        infoService.isMine(seq);
        CommentForm form = infoService.getForm(seq);
        BoardData boardData = form.getBoardData();
        model.addAttribute("boardData",boardData);
        model.addAttribute("commentForm",form);
        model.addAttribute("mode" , "update");
        return utils.tpl("board/update");
    }

    @PostMapping("/save")
    public String save(@Valid CommentForm form , Errors errors , Model model) {
        saveService.save(form,errors);

        if(errors.hasErrors()){
            Map<String , List<String>> messages = Utils.getMessages(errors);
            String message = (new ArrayList<List<String>> (messages.values())).get(0).get(0);
            throw new AlertException(message);
        }


        Long seq = form.getSeq();
        if(seq == null){
            //댓글 작성 완료시 부모창을 새로고침 -> 새로운 목록 갱신
            model.addAttribute("script", "parent.location.reload()");
            return "common/_execute_script";

        }else{//댓글 수정 시 -> 게시글 보기 페이지 이동 -> 해당 댓글 위치로 이동
            return "redirect:/board/view/"+form.getBoardDataSeq()+"#comment_"+seq;
        }



    }
        @RequestMapping("/delete/{seq}")
        public String delete(@PathVariable("seq") Long seq){
            BoardData boardData = commentDeleteService.delete(seq);

            return "redirect:/board/view/" + boardData.getSeq() + "#comments";
        }

        @ExceptionHandler(RequiredPasswordCheckException.class)
        public String guestPassword(){

            return utils.tpl("board/password");
        }

    }




