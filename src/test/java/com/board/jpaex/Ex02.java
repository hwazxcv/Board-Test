package com.board.jpaex;


import com.board.commons.constants.MemberType;
import com.board.entities.BoardData;
import com.board.entities.Member;
import com.board.models.board.BoardListService;
import com.board.repositories.BoardDataRepository;
import com.board.repositories.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.profiles.active=test")

public class Ex02 {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardDataRepository boardDataRepository;
    @PersistenceContext

    private EntityManager em;
    @Autowired
    private BoardListService boardListService;

    @BeforeEach
    void init(){
        Member member = Member.builder()
                .email("user01@test.org")
                .password("123456")
                .userNm("사용자01")
                .mtype(MemberType.USER)
                .mobile("01000000000")
                .build();


                memberRepository.saveAndFlush(member);
        List<BoardData> items= new ArrayList<>();
        for(int i = 1; i<=10; i++){
            BoardData item = BoardData.builder()
                    .subject("제목"+i)
                    .content("내용"+i)
                    .member(member)
                    .build();
            items.add(item);

        }
        boardDataRepository.saveAllAndFlush(items);
        em.clear();
    }

    @Test
    void test1(){
        List<BoardData> items = boardDataRepository.findAll();
        for(BoardData item : items){
            Member member = item.getMember();
            String email = member.getEmail();
            System.out.println(email);

        }
    }

    @Test
    void test2 (){
        List<BoardData> items=boardDataRepository.getList2();

    }

    @Test
    void test3(){
        List<BoardData> items = boardListService.getList();
    }

    @Test
    void test4(){
        List<BoardData> items = boardDataRepository.findBySubjectContaining("목");
    }

    @Test
    void test5(){
        Member member = memberRepository.findByEmail("user01@test.org").orElse(null);
        List<BoardData> items =member.getItems();
        items.stream().forEach(System.out::println);

        memberRepository.delete(member);
        memberRepository.flush();
    }
}
