package com.board.models.board;

import com.board.controllers.boards.BoardForm;
import com.board.entities.BoardData;
import com.board.repositories.BoardDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardInfoService {


    private final BoardDataRepository boardDataRepository;
    //게시글 하나 조회
    public BoardData get(Long seq){
        BoardData data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);

        return data;
    }



}
