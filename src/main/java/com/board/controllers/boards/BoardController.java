package com.board.controllers.boards;

import com.board.commons.ListData;
import com.board.commons.MemberUtil;
import com.board.commons.ScriptExceptionProcess;
import com.board.commons.Utils;
import com.board.commons.constants.BoardAuthority;
import com.board.commons.exceptions.AlertBackException;
import com.board.entities.Board;
import com.board.entities.BoardData;
import com.board.models.board.BoardDataNotFoundException;
import com.board.models.board.BoardDeleteService;
import com.board.models.board.BoardInfoService;
import com.board.models.board.BoardSaveService;
import com.board.models.board.config.BoardConfigInfoService;
import com.board.models.board.config.BoardNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final BoardDeleteService deleteService;
    private final BoardConfigInfoService configInfoService;

    private BoardData boardData;


    @GetMapping("/write/{bId}")
    public String write(@PathVariable("bId") String bId, @ModelAttribute BoardForm form, Model model){ // 게시판 쓰기
        commonProcess(bId,"write", model);

        if(memberUtil.isLogin()){
            form.setPoster(memberUtil.getMember().getUserNm());
        }

        form.setBId(bId); //경로 변수에서들어온 bId를 넣어줌

        return utils.tpl("board/write"); //모바일 혹은 PC연동
    }


    @GetMapping("/update/{seq}")
    public String update(@PathVariable Long seq , Model model) { // 게시판 수정
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
    public String view(@PathVariable("seq") Long seq, @ModelAttribute BoardDataSearch search, Model model){

        BoardData data = infoService.get(seq);
        boardData = data;
        String bId = data.getBoard().getBId;
        commonProcess(bId , "view" ,model);
        search.setBId(bId);

        ListData<BoardData> listData  = infoService.getList(search);
        model.addAttribute("boardData",data);
        model.addAttribute("items",listData.getContent());
        model.addAttribute("pagination",listData.getPagination());
        return utils.tpl("board/view");
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq){ //게시판 삭제 -> 이후에 페이지 이동
//        if(!infoService.inMine(seq)){
//            throw new AlertBackException(Utils.getMessage("작성한_게시글만_삭제가능합니다","error"));
//        }

        BoardData data= infoService.get(seq);
        deleteService.delete(seq);
        return "redirect:/board/list/"+data.getBoard().getBId();
    }

    @GetMapping("/list/{bId}")
    public String list(@PathVariable("bId") String bId/*,@ModelAttribute BoardDataSerch serch*/ , Model model ){
        commonProcess(bId , "list", model);
//        search.setBid(bId);
//        ListData<BoardData> data = infoService.getList(search);
//        model.addAttribute("items" , data.getContent());
//        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("board/list");

    }

    @PostMapping("/guest/password")
    public String guestPasswordCheck(@RequestParam("password") String password , HttpSession session , Model model){
        Long seq = (Long)session.getAttribute("guest_seq");
        if(seq == null){
            throw new BoardDataNotFoundException();
        }

        if(!infoService.checkGuestPassword(seq, password)){//비번검증 실패시
            throw new AlertBackException(Utils.getMessage("비밀번호가_일치하지_않습니다.","error"));
        }
        //검증 성공시
        String key="chk_"+seq;
        session.setAttribute(key,true);
        session.removeAttribute("guest_seq");

        model.addAttribute("script" , "parent.location.reload()");
        return "common/_execute_script";
    }

    //페이지마다 변경될 공통 속성
    private void commonProcess( String bId , String mode, Model model ){
        Board board = configInfoService.get(bId);

        //게시판이 등록 되지 않거나 미사용 중 게시판일때(isActive가 false이면) 예외(관리자는 들어가지게 설정)
        if(board == null || !board.isActive() && !memberUtil.isAdmin()){
            throw new BoardNotFoundException();
        }
        /*게시판 분류(윈도우에서 줄개행 문자가 생김)*/
        String category = board.getCategory();
        List<String> categories = StringUtils.hasText(category) ?
                Arrays.stream(category.trim().split("\\n"))
                        .map(s -> s.replaceAll("\\r", "")).toList():null;
        model.addAttribute("categories" , categories);


        String bName= board.getBName();
        String pageTitle= bName;

        if(mode.equals("write")) pageTitle=bName+"작성";
        else if (mode.equals("update")) pageTitle=bName+"수정";
        else if (mode.equals("view") && boardData!=null){
            pageTitle = boardData.getSubject()+"||"+bName;

        }

        /**
         * 글쓰기 수정시 권한 체크
         * */
        if(mode.equals("Write") || mode.equals("update")){

        }

        BoardAuthority authority = board.getAuthority();
        if(!memberUtil.isAdmin() && !memberUtil.isLogin() && authority == BoardAuthority.MEMBER){ //(회원 전용)관리자가 아니고 로그인이 아니고 권한이 MEMBER이면 (관리자는 가능)
            throw new AlertBackException(Utils.getMessage("MemberOnly.board","error"));
        }
        if(authority == BoardAuthority.ADMIN && !memberUtil.isAdmin()){ // 관리자전용
            throw new AlertBackException(Utils.getMessage("AdminOnly.board","error"));
        }


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
        model.addAttribute("board", board);
    }


}
