package com.board.models.board.config;

import com.board.commons.Utils;
import com.board.commons.exceptions.AlertException;
import com.board.entities.Board;
import com.board.repositories.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardConfigDeleteService {

    private final Utils utils;
    private final BoardRepository repository;

    //게시판 단일 삭제 기능
    public void delete(String bId){
        //삭제시 bId가 없으면 예외 발생
        Board board= repository.findById(bId).orElseThrow(BoardNotFoundException::new);
        //있으면 삭제
        repository.delete(board);
        repository.flush();
    }

    //게시판 목록에서 일괄 삭제
    public void delete(List<Integer> idxes){

        if(idxes ==null || idxes.isEmpty()){
            throw new AlertException("삭제할 게시판을 선택하세요" , HttpStatus.BAD_REQUEST);

        }

        for(int idx : idxes){
            String bId= utils.getParam("bId_"+idx);
            Board board= repository.findById(bId).orElse(null);
            if(board == null) continue;

            repository.delete(board);
        }
        repository.flush();

    }

}
