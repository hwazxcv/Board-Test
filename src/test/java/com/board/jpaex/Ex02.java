package com.board.jpaex;


import com.board.commons.constants.MemberType;
import com.board.entities.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class Ex02 {

    @PersistenceContext
    private EntityManager em;

    @Test
    void test1(){
        Member member = Member.builder()
                .email("userO1@test.org")
                .password("123456")
                .userNm("사용자01")
                .mobile("01011111111")
                .mtype(MemberType.USER)
                .build();

        em.persist(member);
        em.flush();

        Member member2 = em.find(Member.class,member.getUserNo());
        System.out.println(member2); //수정전

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        member2.setUserNm("(수정)사용자01");
        em.flush();

        member2 = em.find(Member.class,member.getUserNo());
        System.out.println(member2); // 수정후
    }

    @Test
    void test2(){

    }
}
