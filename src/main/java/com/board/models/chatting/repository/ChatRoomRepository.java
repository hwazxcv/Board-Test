package com.board.models.chatting.repository;

import com.board.models.chatting.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatRoomRepository extends JpaRepository<ChatRoom , String>, QuerydslPredicateExecutor<ChatRoom> {
}
