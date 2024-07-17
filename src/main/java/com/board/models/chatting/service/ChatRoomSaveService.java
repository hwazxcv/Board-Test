package com.board.models.chatting.service;

import com.board.commons.MemberUtil;
import com.board.models.chatting.controller.RequestChatRoom;
import com.board.models.chatting.entity.ChatRoom;
import com.board.models.chatting.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomSaveService {


    private final ChatRoomRepository chatRoomRepository;
    private final MemberUtil memberUtil;



    public ChatRoom save(RequestChatRoom form){

        String roomId = form.getRoomId();

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseGet(() -> ChatRoom.builder()
                        .roomId(roomId)
                        .member(memberUtil.getMember())
                        .build());

        room.setRoomNm(form.getRoomNm());
        room.setCapacity(form.getCapacity());

        chatRoomRepository.saveAndFlush(room);
        return room;

    }

}
