package com.board.models.chatting.service;

import com.board.commons.ListData;
import com.board.commons.Pagination;
import com.board.commons.Utils;
import com.board.models.chatting.controller.ChatRoomSearch;
import com.board.models.chatting.entity.ChatRoom;
import com.board.models.chatting.entity.QChatRoom;
import com.board.models.chatting.repository.ChatRoomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomInfoService {

    private final ChatRoomRepository chatRoomRepository;
    private final HttpServletRequest request;

    public ChatRoom get(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomNotFoundException::new);
        return chatRoom;
    }

    public ListData<ChatRoom> getList(ChatRoomSearch search){
        int page = Utils.getNumber(search.getPage(),1);
        int limit = Utils.getNumber(search.getLimit() , 20);

        QChatRoom chatRoom = QChatRoom.chatRoom;
        BooleanBuilder andBuilder = new BooleanBuilder();

        String sopt =search.getSopt();
        String skey = search.getSkey();

        if(StringUtils.hasText(skey)){
            skey = skey.trim();
            BooleanExpression roomNmCond = chatRoom.roomNm.contains(skey);
            BooleanExpression roomIdCond = chatRoom.roomId.contains(skey);

            if(sopt.equals("roomNm")){
                andBuilder.and(roomIdCond);
            } else if (sopt.equals("roomId")) {
                andBuilder.and(roomIdCond);
            }else {
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(roomNmCond).or(roomIdCond);
                andBuilder.and(orBuilder);
            }
        }

        Pageable pageable = PageRequest.of(page -1 , limit , Sort.by(desc("createdAt")));
        Page<ChatRoom> data = chatRoomRepository.findAll(andBuilder,pageable);
        int total = (int)chatRoomRepository.count(andBuilder);
        Pagination pagination = new Pagination(page, total , 10 , limit , request);
        return new ListData<>(data.getContent(),pagination);

    }





}
