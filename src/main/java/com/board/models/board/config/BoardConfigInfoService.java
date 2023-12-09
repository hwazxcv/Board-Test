package com.board.models.board.config;

import com.board.commons.ListData;
import com.board.commons.Pagination;
import com.board.commons.Utils;
import com.board.commons.constants.BoardAuthority;
import com.board.controllers.admins.BoardConfigForm;
import com.board.controllers.admins.BoardSearch;
import com.board.entities.Board;
import com.board.entities.QBoard;
import com.board.repositories.BoardRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

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

    public BoardConfigForm getForm(String bId){
        Board board = get(bId);
        BoardConfigForm form = new ModelMapper().map(board,BoardConfigForm.class);
        form.setAuthority(board.getAuthority().name());
        form.setMode("edit");
        return form;

    }
    //게시판 목록 조회( common -> ListData<T> 이용)
    //BoardSearch 전달용객체를 매개변수로
    public ListData<Board> getList(BoardSearch search){
        BooleanBuilder andBuilder = new BooleanBuilder( );//조건식

        int page = Utils.getNumber(search.getPage() , 1) ;
        int limit= Utils.getNumber(search.getLimit(), 20);
        
        //검색 처리
        //QBoard를 통한 and,or  조건 처리
        QBoard qBoard = QBoard.board;
        //키워드 검색
        String sopt = Objects.requireNonNull(search.getSopt(),"ALL");
        String skey= search.getSkey();
        if(StringUtils.hasText(skey)){
            skey = skey.trim();
            if(sopt.equals("bId")){ // 게시판 아이디
                andBuilder.and(qBoard.bId.contains(skey));
            } else if (sopt.equals("bName")) { //게시판이름
                andBuilder.and(qBoard.bName.contains(skey));
            }else{ //통합검색
                BooleanBuilder orBuilder=new BooleanBuilder();
                orBuilder.or(qBoard.bId.contains(skey)).or(qBoard.bName.contains(skey));
                andBuilder.and(orBuilder);
            }
        }
        //사용 여부
        List<Boolean> active = search.getActive();
        if(active != null && !active.isEmpty()){
            andBuilder.and(qBoard.active.in(active));
        }
        //글쓰기 권한
        List<BoardAuthority> authorities = search.getAuthority() == null ?
        null:search.getAuthority().stream().map(BoardAuthority::valueOf).toList();

        if(authorities!=null && authorities.isEmpty()){
            andBuilder.and(qBoard.authority.in(authorities));
        }


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
