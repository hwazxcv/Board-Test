package com.board.models.board;

import com.board.controllers.boards.BoardForm;
import com.board.entities.BoardData;
import com.board.repositories.BoardDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
@RequiredArgsConstructor
public class BoardSaveService {

    private final BoardDataRepository boardDataRepository;



    //boardForm이라는 커맨드객체가 들어옴
    public void save(BoardForm form){
        Long seq = form.getSeq();
        String mode = Objects.requireNonNullElse(form.getMode(),"add");

        BoardData data = null;
        //수정과 추가
        if (mode.equals("update") && seq!=null){
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);
        }else {
            data = new BoardData();
        }
        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setPoster(form.getPoster());
        boardDataRepository.saveAndFlush(data);
    }

}
