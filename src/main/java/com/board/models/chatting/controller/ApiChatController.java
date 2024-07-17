package com.board.models.chatting.controller;


import com.board.commons.ListData;
import com.board.commons.exceptions.CommonRestException;
import com.board.commons.rest.JSONData;
import com.board.models.chatting.entity.ChatHistory;
import com.board.models.chatting.entity.ChatRoom;
import com.board.models.chatting.service.ChatHistoryInfoService;
import com.board.models.chatting.service.ChatHistorySaveService;
import com.board.models.chatting.service.ChatRoomInfoService;
import com.board.models.chatting.service.ChatRoomSaveService;
import com.board.restcontrollers.CommonRestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ApiChatController implements CommonRestController {

    private final ChatRoomInfoService chatRoomInfoService;
    private final ChatRoomSaveService chatRoomSaveService;
    private final ChatHistoryInfoService chatHistoryInfoService;
    private final ChatHistorySaveService chatHistorySaveService;


    /**
     * 방목록 조회
     * @param search
     * @return
     */
    @GetMapping("/room")
    public JSONData<ListData<ChatRoom>> getRoom(@RequestBody ChatRoomSearch search){
        ListData<ChatRoom> data = chatRoomInfoService.getList(search);

        return new JSONData<>(data);
    }

    @PostMapping("/room")
    public ResponseEntity saveRoom(@RequestBody @Valid RequestChatRoom form , Errors errors){

        if(errors.hasErrors()){
            throw new CommonRestException(errors , HttpStatus.BAD_REQUEST);
        }

        chatRoomSaveService.save(form);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/room/{roomId}")
    public JSONData<ChatRoom> getRoom(@PathVariable("roomId") String roomId){
        ChatRoom data = chatRoomInfoService.get(roomId);
        return new JSONData<>(data);
    }

    /**
     *  채팅방 메세지 기록 저장
     * @param form
     * @param errors
     * @return
     */
    @PostMapping
    public ResponseEntity messageSave (@RequestBody @Valid RequestChatHistory form , Errors errors){

        if(errors.hasErrors()){
            throw new CommonRestException(errors , HttpStatus.BAD_REQUEST);
        }
        chatHistorySaveService.save(form);
        return  ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/messages/{roomId}")
    public JSONData<List<ChatHistory>> getMessages(@PathVariable("roomId") String roomId, @RequestBody ChatHistorySearch search){
        List<ChatHistory> messages = chatHistoryInfoService.getList(roomId , search);
        return new JSONData<>(messages);
    }

}
