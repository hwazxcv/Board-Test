package com.board.controllers.main;

import com.board.commons.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final Utils utils;


    @GetMapping("/")
    public String index(){
        return utils.tpl("main/index");
    }


}
