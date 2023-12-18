package com.board.models.comment;

import com.board.entities.BoardData;
import com.board.entities.CommentData;
import com.board.repositories.CommentDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDeleteService {
    private final CommentInfoService infoService;
    private final CommentDataRepository repository;


    public BoardData delete(Long seq){
        infoService.isMine(seq);
        CommentData commentData = infoService.get(seq);
        BoardData boardData = commentData.getBoardData();

        repository.delete(commentData);
        repository.flush();

        Long boardDataSeq =boardData.getSeq();
        infoService.updateCommentCnt(boardDataSeq);

        return boardData;
    }
}
