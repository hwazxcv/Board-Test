package com.board.models.center.controller;

import com.board.commons.ExceptionProcessor;
import com.board.commons.ListData;
import com.board.commons.menus.Menu;
import com.board.commons.menus.MenuDetail;
import com.board.models.center.RequestCenter;
import com.board.models.center.entity.CenterInfo;
import com.board.models.center.service.CenterDeleteService;
import com.board.models.center.service.CenterInfoService;
import com.board.models.center.service.CenterSaveService;
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
@RequiredArgsConstructor
@RequestMapping("admin/center")
public class CenterAdminController implements ExceptionProcessor {

    private final CenterInfoService infoService;
    private final CenterSaveService saveService;
    private final CenterDeleteService deleteService;

    @ModelAttribute("menuCode")
    public String getMenuCode(){
        return "center";
    };

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus(){
        return Menu.gets("center");
    }

    @GetMapping
    public String list(@ModelAttribute CenterSearch search , Model model){
        commonProcess("list" ,model);
        ListData<CenterInfo> data = infoService.getList(search);

        model.addAttribute("items",data.getContent());
        model.addAttribute("pagination" , data.getPagination());
        return "admin/center/list";
    }

    @PatchMapping
    public String editList(@RequestParam(name = "chk", required = false) List<Integer> chks , Model model){
        saveService.saveList(chks);
        model.addAttribute("script" , "parent.location.reload()");
        return "common/_execute_script";
    }

    @DeleteMapping
    public String deleteList(@RequestParam(name="chk" , required = false) List<Integer> chks , Model model){

        deleteService.deleteList(chks);
        model.addAttribute("script" , "parent.location.reload()");
        return "common/_execute_script";
    }


    @GetMapping("/add_center")
    public String addCenter(@ModelAttribute RequestCenter form , Model model){
        commonProcess("add_center" , model);
        return "admin/center/add_center";
    }

    @GetMapping("edit_center/{cCode}")
    public String editCenter(@PathVariable("cCode") Long cCode , Model model){
        commonProcess("edit_center" , model);
        RequestCenter form = infoService.getForm(cCode);
        model.addAttribute("requestCenter" , form);
        return "admin/center/edit_center";
    }

    @PostMapping("/save_center")
    public String saveCenter(@Valid RequestCenter form , Errors errors , Model model){
        String mode = form.getMode();
        commonProcess(mode,model);
        if(errors.hasErrors()){
            return "admin/center/"+mode;
        }
        CenterInfo data = saveService.save(form);
        return "redirect:/admin/center/info_center"+data.getCCode();
    }

    @GetMapping("/info_center/{cCode}")
    public String infoCenter(@PathVariable("cCode") Long cCode , Model model){
        commonProcess("info_center" , model);
        CenterInfo data = infoService.get(cCode);

        model.addAttribute("centerInfo",data);

        return "admin/center/info_center";
    }


    /**
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode , Model model){
        String pageTitle  = "센터 목록";
        mode = Objects.requireNonNullElse(mode,"list");
        List<String> addCommonScript = new ArrayList<>();

        if(mode.equals("add_center") || mode.equals("edit_center")){
            pageTitle = "뉴 센터";
            pageTitle +=mode.contains("edit") ? "수정" : "등록";
            addCommonScript.add("address");
        } else if(mode.equals("info_center")){
            pageTitle = "센터 정보";
        }
        model.addAttribute("pageTitle" , pageTitle);
        model.addAttribute("addCommonScript" , addCommonScript);
        model.addAttribute("subMenuCode",mode);

    }

}
