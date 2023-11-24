package com.board.controllers.admins;

import com.board.commons.menus.Menu;
import com.board.models.board.config.BoardConfigSaveService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller("adminBoardController")
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class BoardController {

    //menu 코드 의존성
    private final HttpServletRequest request;

    private final BoardConfigSaveService saveService;

    @GetMapping
    public String list(Model model){

        commonProcess("list" , model);

        return "admin/board/list";
    }


    @GetMapping("/add")
    //                      비어있는 객체라도 자동으로 들어감
    public String register(@ModelAttribute BoardConfigForm form , Model model){
        commonProcess("add", model);


        return "admin/board/add";
    }

    @GetMapping("/edit/{dId}")
    public String update(@PathVariable String bId , Model model){

         commonProcess("edit", model);

        return "admin/board/edit";
    }

    @PostMapping("/save")
    //
    public String save(@Valid BoardConfigForm form , Errors errors , Model model){

        String mode = Objects.requireNonNullElse(form.getMode() , "add");
        commonProcess(mode,model);
        if(errors.hasErrors()){
            return "admin/board/"+mode;
        }

        saveService.save(form);

        return "redirect:/admin/board";
    }

    // 공통 모드 로직(add 와 edit을 같이 쓰려고 )
    private void commonProcess(String mode ,Model model){
        String pageTitle = "게시판 목록";
        //값이 없으면 list로 반환
        mode = Objects.requireNonNullElse(mode,"list");
        if(mode.equals("add")) pageTitle  ="게시판 등록";
        else if (mode.equals("edit")) pageTitle="게시판 수정";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("menuCode", "board");
        model.addAttribute("submenus", Menu.gets("board"));
        model.addAttribute("subMenuCode" , Menu.getSubMenuCode(request));
    }

}
