package com.board.models.board.config;

import com.board.commons.ListData;
import com.board.commons.Pagination;
import com.board.commons.Utils;
import com.board.controllers.admins.BoardConfigForm;
import com.board.controllers.admins.BoardSearch;
import com.board.entities.Board;
import com.board.repositories.BoardRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.Order.desc;

@Service //admin board 기능 구현
@RequiredArgsConstructor

public class BoardConfigInfoService { //관리자 페이지 게시판 목록 관리

    private final BoardRepository repository;
    private final HttpServletRequest request;



    //게시판 단일 조회
    public Board get(String  bId){
        //조회시 게시판 아이디 값이 없으면 예외
        Board data = repository.findById(bId).orElseThrow(BoardNotFoundException::new);
        return data;
    }
    //게시판 목록 조회( common -> ListData<T> 이용)
    //BoardSearch 전달용객체를 매개변수로
    public ListData<Board> getList(BoardSearch search){
        BooleanBuilder andBuilder = new BooleanBuilder( );//조건식

        int page = Utils.getNumber(search.getPage() , 1) ;
        int limit= Utils.getNumber(search.getLimit(), 20);

        //Sort.Order.desc ("엔티티 속성명") -- asc동일
        Pageable pageable = PageRequest.of(page -1, limit, Sort.by(desc("createdAt")));

        Page<Board> data=  repository.findAll(andBuilder , pageable);

        Pagination pagination = new Pagination(page, (int)data.getTotalPages(), 10,limit, request);

        ListData<Board> listData = new ListData<>();
        listData.setContent(data.getContent());
        listData.setPagination(pagination);
        return listData;
    }



}
