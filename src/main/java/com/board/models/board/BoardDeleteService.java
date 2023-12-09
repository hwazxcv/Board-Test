package com.board.models.board;


import com.board.entities.BoardData;
import com.board.models.file.FileDeleteService;
import com.board.repositories.BoardDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardDeleteService {

    private final BoardInfoService infoService;
    private final BoardDataRepository repository;
    private final FileDeleteService fileDeleteService;

    public void delete(Long seq){
        BoardData data = infoService.get(seq);
        String gid = data.getGid();
        //파일 삭제
        fileDeleteService.deleteByGid(gid);

        //게시글 삭제
        repository.delete(data);
        repository.flush();


    }



}
