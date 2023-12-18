package com.board.repositories;

import com.board.entities.CommentData;
import com.board.entities.QCommentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentDataRepository extends JpaRepository<CommentData, Long>, QuerydslPredicateExecutor<CommentData> {



    //게시글 댓글 수
    default int getTotal(Long boardDataSeq){
        QCommentData commentData = QCommentData.commentData;
        return (int)count(commentData.boardData.seq.eq(boardDataSeq));
    }
}
