package com.board.tests;


import com.board.entities.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest

public class JSONTest {

    private ObjectMapper om;

    @BeforeEach
    void init(){

        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
    }


    @Test
    void test1() throws JsonProcessingException {
        Member member = Member.builder()
                .email("user")
                .password("123456")
                .userNm("사용자01")
                .build();
        member.setCreateAt(LocalDateTime.now());

        //json 형식으로 변환
        String json = om.writeValueAsString(member);
        System.out.println(json);

        //단일 객체를 클래스가 매개변수이다.
        Member member2 = om.readValue(json, Member.class);
    }
    void test2() throws JsonProcessingException {
        List<Member> members = new ArrayList<>();
        for(int i =1; i<=3; i++){
            Member member = Member.builder()
                    .email("user")
                    .password("123456")
                    .userNm("사용자01")
                    .build();

            members.add(member);
        }
        //json 형식 변환
        String json = om.writeValueAsString(members);
        //단일 객체가 아니 중첩된 형태일때  TypeReference를 사용한다.
        List<Member> members2 = om.readValue(json, new TypeReference<List<Member>>() {});


    }
}
