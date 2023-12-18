package com.board.controllers.boards;

import com.board.commons.ListData;
import com.board.commons.MemberUtil;
import com.board.commons.ScriptExceptionProcess;
import com.board.commons.Utils;
import com.board.commons.constants.BoardAuthority;
import com.board.commons.exceptions.AlertBackException;
import com.board.entities.Board;
import com.board.entities.BoardData;
import com.board.entities.FileInfo;
import com.board.models.board.*;
import com.board.models.board.config.BoardConfigInfoService;
import com.board.models.board.config.BoardNotFoundException;
import com.board.models.comment.CommentInfoService;
import com.board.models.file.FileInfoService;
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

@Controller("Controller2")
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
    private final FileInfoService fileInfoService;
    private final CommentInfoService commentInfoService;


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
    public String update(@PathVariable("seq") Long seq, Model model) { // 게시글 수정
        if (!infoService.isMine(seq)) { // 직접 작성한 게시글이 아닌 경우
            throw new AlertBackException(Utils.getMessage("작성한_게시글만_수정할_수_있습니다.", "error"));
        }

        BoardForm form = infoService.getForm(seq);

        commonProcess(form.getBId(), "update", model);

        model.addAttribute("boardForm", form);

        return utils.tpl("board/update");
    }

    @PostMapping("/save")
    public String save(@Valid BoardForm form, Errors errors, Model model) {//게시물저장 -> 이후에 페이지 이동
        String mode = Objects.requireNonNullElse(form.getMode(), "write");
        String bId = form.getBId();

        commonProcess(bId, mode, model);

        if (mode.equals("update")) {
            Long seq = form.getSeq();
            if (!infoService.isMine(seq)) { // 직접 작성한 게시글이 아닌 경우
                throw new AlertBackException(Utils.getMessage("작성한_게시글만_수정할_수_있습니다.", "error"));
            }
        }
        saveService.save(form, errors);

        if (errors.hasErrors()) {
            String gid = form.getGid();
            List<FileInfo> editorImages = fileInfoService.getListAll(gid, "editor");
            List<FileInfo> attachFiles = fileInfoService.getListAll(gid, "attach");
            form.setEditorImages(editorImages);
            form.setAttachFiles(attachFiles);

            return utils.tpl("board/" + mode);
        }
        return "redirect:/board/list/" + bId;
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq,
                       @ModelAttribute  BoardDataSearch search, Model model) {

        // 게시글 조회수 업데이트
        infoService.updateView(seq);

        BoardData data = infoService.get(seq);
        boardData = data;

        String bId = data.getBoard().getBId();
        commonProcess(bId, "view", model);

        search.setBId(bId);
        ListData<BoardData> listData = infoService.getList(search);

        model.addAttribute("boardData", data);
        model.addAttribute("items", listData.getContent());
        model.addAttribute("pagination", listData.getPagination());

        return utils.tpl("board/view");
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq){ //게시판 삭제 -> 이후에 페이지 이동
       if(!infoService.isMine(seq)){
           throw new AlertBackException(Utils.getMessage("작성한_게시글만_삭제가능합니다","error"));
        }
        BoardData data= infoService.get(seq);
        deleteService.delete(seq);
        return "redirect:/board/list/"+data.getBoard().getBId();
    }

    @GetMapping("/list/{bId}")
    public String list(@PathVariable("bId") String bId,@ModelAttribute  BoardDataSearch search, Model model ){
        commonProcess(bId , "list", model);
        search.setBId(bId);
        ListData<BoardData> data = infoService.getList(search);
        model.addAttribute("items" , data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("board/list");

    }

    @PostMapping("/guest/password")
    public String guestPasswordCheck(@RequestParam("password") String password , HttpSession session , Model model){

        Long seq = (Long)session.getAttribute("guest_seq");
        //댓글 비회원 비밀번호 확인
        Long commentSeq = (Long)session.getAttribute("comment_seq");
        if(commentSeq != null){
            return guestCommentPasswordCheck(commentSeq,password , session , model);
        }

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

    private String guestCommentPasswordCheck(Long seq , String password , HttpSession session , Model model){

        if(seq == null){

            throw new BoardDataNotFoundException();
        }

        if(!commentInfoService.checkGuestPassword(seq, password)){//비번검증 실패시
            throw new AlertBackException(Utils.getMessage("비밀번호가_일치하지_않습니다.","error"));
        }
        //검증 성공시
        String key ="chk_comment"+seq;
        session.setAttribute(key,true);
        session.removeAttribute("comment_seq");

        model.addAttribute("script" , "parent.location.reload()");
        return "common/_execute_script";
    }

    //페이지마다 변경될 공통 속성
    private void commonProcess( String bId , String mode, Model model ){
        Board board = configInfoService.get(bId);

        //게시판이 등록 되지 않거나 미사용 중 게시판일때(isActive가 false이면) 예외(관리자는 들어가지게 설정)
        if (board == null || (!board.isActive() && !memberUtil.isAdmin())) {
            throw new BoardNotFoundException();
        }
        /*게시판 분류(윈도우에서 줄개행 문자가 생김)*/
        String category = board.getCategory();
        List<String> categories = StringUtils.hasText(category) ?
                Arrays.stream(category.trim().split("\\n"))
                        .map(s -> s.replaceAll("\\r", "")).toList():null;
        model.addAttribute("categories", categories);


        String bName = board.getBName();
        String pageTitle = bName;
        if (mode.equals("write")) pageTitle = bName + " 작성";
        else if (mode.equals("update")) pageTitle = bName + " 수정";
        else if (mode.equals("view") && boardData != null) {
            pageTitle = boardData.getSubject() + "||" + bName;
        }

        // 글쓰기 수정시 권한 체크

        if (mode.equals("write") || mode.equals("update")) {
            BoardAuthority authority = board.getAuthority();
            if (!memberUtil.isAdmin() && !memberUtil.isLogin()
                    && authority == BoardAuthority.MEMBER) { // 회원 전용
                throw new AlertBackException(Utils.getMessage("MemberOnly.board", "error"));
            }

            if (authority == BoardAuthority.ADMIN && !memberUtil.isAdmin()) { // 관리자 전용
                throw new AlertBackException(Utils.getMessage("AdminOnly.board", "error"));
            }
        }


        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        if (mode.equals("write") || mode.equals("update")) {
            addCommonScript.add("ckeditor/ckeditor");
            addCommonScript.add("fileManager");

            addScript.add("board/form");
        }
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("board", board);
    }

    @ExceptionHandler(RequiredPasswordCheckException.class)
    public String guestPassword() {

        return utils.tpl("board/password");
    }


}
