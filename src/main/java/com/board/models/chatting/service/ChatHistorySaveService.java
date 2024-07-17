package com.board.models.chatting.service;

import com.board.commons.MemberUtil;
import com.board.models.chatting.controller.RequestChatHistory;
import com.board.models.chatting.entity.ChatHistory;
import com.board.models.chatting.entity.ChatRoom;
import com.board.models.chatting.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatHistorySaveService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatRoomInfoService chatRoomInfoService;
    private final MemberUtil memberUtil;




    public void save(RequestChatHistory form){

        ChatRoom room  = chatRoomInfoService.get(form.getRoomId());

        ChatHistory data = ChatHistory.builder()
                .member(memberUtil.getMember())
                .chatRoom(room)
                .nickName(form.getNicName())
                .message(form.getMessage())
                .build();

        chatHistoryRepository.saveAndFlush(data);

    }
}
