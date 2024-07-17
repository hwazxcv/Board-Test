package com.board.models.chatting.repository;

import com.board.models.chatting.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory , Long> , QuerydslPredicateExecutor<ChatHistory> {
}
