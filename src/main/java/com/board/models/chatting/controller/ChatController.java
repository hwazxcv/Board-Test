package com.board.models.chatting.controller;


import com.board.commons.ExceptionProcessor;
import com.board.commons.ListData;
import com.board.commons.MemberUtil;
import com.board.commons.Utils;
import com.board.entities.Member;
import com.board.models.chatting.entity.ChatHistory;
import com.board.models.chatting.entity.ChatRoom;
import com.board.models.chatting.service.ChatHistoryInfoService;
import com.board.models.chatting.service.ChatRoomInfoService;
import com.board.models.chatting.service.ChatRoomSaveService;
import com.board.repositories.MemberRepository;
import com.board.restcontrollers.MemberNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatting")
public class ChatController implements ExceptionProcessor {


    private final ChatRoomInfoService chatRoomInfoService;
    private final ChatRoomSaveService chatRoomSaveService;
    private final ChatHistoryInfoService chatHistoryInfoService;
    private final MemberRepository repository;

    private final MemberUtil memberUtil;
    private final Utils utils;
    private ChatRoom chatRoom;


    @GetMapping
    public String roomList(@ModelAttribute ChatRoomSearch search , Model model){


        commonProcess("room_list", model);

        ListData<ChatRoom> data  = chatRoomInfoService.getList(search);

        model.addAttribute("items",data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("chat/rooms");
    }

    @GetMapping("/create")
    public String createRoom(@ModelAttribute RequestChatRoom form , Model model){
        commonProcess("create_room",model);

        return utils.tpl("chat/create_room");
    }


    @PostMapping("/create")
    public String createRoomPs(@Valid RequestChatRoom form  , Errors errors , Model model) {
        commonProcess("create_room", model);

        if(errors.hasErrors()){
            return utils.tpl("chat/create_room");
        }
        chatRoomSaveService.save(form);
        return "redirect:/catting"+form.getRoomId();
    }



    @GetMapping("/apply/{userId}")
    public String applyChatting(@PathVariable("userId")  String userId){

        if(!repository.existsByUserId(userId)){
            throw new MemberNotFoundException();
        }

        if(!memberUtil.isLogin()){
            return "redirect:/member/login?redirectURL=/chtting/apply"+userId;
        }


        Member member = memberUtil.getMember();
        String roomId = member.getUserId()+"_"+userId;
        String roomNm = "1:1채팅";
        RequestChatRoom form = new RequestChatRoom();
        form.setRoomId(roomId);
        form.setRoomNm(roomNm);
        form.setCapacity(2);

        chatRoomSaveService.save(form);
        return "redirect:/chatting/"+roomId;
    }



    //채팅방
    @GetMapping("/{roomId}")
    public String chattingRoom(@PathVariable("roomId") String roomId , Model model){
        commonProcess(roomId ,"chat_room",model);

        List<ChatHistory> items = chatHistoryInfoService.getList(roomId);

        model.addAttribute("items", items);

        return utils.tpl("chat/room");

    }



    private void commonProcess(String roomId , String mode , Model model){
        chatRoom = chatRoomInfoService.get(roomId);
        commonProcess(mode , model);

        model.addAttribute("chatRoom", chatRoom);
    }


    private void commonProcess(String mode , Model model){
        mode = StringUtils.hasText(mode) ? mode : "room_list";
        String pageTitle = Utils.getMessage("채팅방 목록" , "commons");

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();

        addCss.add("chat/style");

        if(mode.equals("create_room")){
            pageTitle  = Utils.getMessage("채팅방 생성" , "commons");
        }else if(mode.equals("chat_room")){
            pageTitle = chatRoom.getRoomNm();
            addScript.add("chat/room");
        }

        model.addAttribute("addCommonScript" ,addCommonScript);
        model.addAttribute("addScript" , addScript);
        model.addAttribute("pageTitle", pageTitle);
    }


}
