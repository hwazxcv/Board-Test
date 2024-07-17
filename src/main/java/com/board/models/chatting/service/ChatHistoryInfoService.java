package com.board.models.chatting.service;


import com.board.models.chatting.controller.ChatHistorySearch;
import com.board.models.chatting.entity.ChatHistory;
import com.board.models.chatting.entity.QChatHistory;
import com.board.models.chatting.repository.ChatHistoryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryInfoService {


    private final ChatHistoryRepository chatHistoryRepository;

    private final EntityManager em;

    public List<ChatHistory> getList(String roomId , ChatHistorySearch search) {

        QChatHistory chatHistory = QChatHistory.chatHistory;
        BooleanBuilder andBuilder = new BooleanBuilder();

        andBuilder.and(chatHistory.chatRoom.roomId.eq(roomId));

        // PathBuilder는 QueryDsl에서 동적으로 쿼리를 생성할때 사용 된다
        // PathBuilder는 제넥릭 클래스 이며 데이터 베이스 테이블의 각컬럼을 표현하는 "Path" 객체들을 동적으로 생성 하게 할 수 있다.
        // new PathBuilder(데이터 베이스 테이블과 매핑된 클래스 , 테이블의 별칭(alias) )
        PathBuilder<ChatHistory> pathBuilder = new PathBuilder<>(ChatHistory.class, "chatHistory");

        List<ChatHistory> items = new JPAQueryFactory(em)
                .selectFrom(chatHistory)
                .leftJoin(chatHistory.member)
                .fetchJoin()
                .where(andBuilder)
                // QrderSpecifier은 QueryDsl에서 OrderBy절을 구성하기 위해 사용됨 (정렬)
                // PathBuilder와 함께 동적 정렬 조건을 설정하는데 사용한다.
                .orderBy(new OrderSpecifier(Order.ASC, pathBuilder.get("createdAt")))
                .fetch();
        return items;
    }

    public List<ChatHistory> getList(String roomId){
        ChatHistorySearch search = new ChatHistorySearch();
        return getList(roomId , search);
        }


}
