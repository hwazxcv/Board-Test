package com.board.jpaex;


import com.board.commons.constants.MemberType;
import com.board.entities.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
//설정 파일 입력
//@TestPropertySource()
public class Ex01 {


    @PersistenceContext
    //Jpa와 관련된 어노테이션 (의존성 주입)

    private EntityManager em;

    @BeforeEach
    void init(){
        Member member = new Member();
       // member.setUserNo(1L);
        member.setEmail("user01@test.org");
        member.setUserNm("사용자01");
        member.setPassword("123456");
        member.setMobile("01000000000");
        member.setMtype(MemberType.USER);

        //영속성 컨텍스트 안에다가 넣어주는 메서드(persist)
        em.persist(member);
        //db에 추가
        //insert쿼리 수행
        em.flush();
        em.clear(); // 영속성 비우기
    }
    @Test
    void test2(){
        Member member = em.find(Member.class , 1L); // DB에서 영속성 컨텍스트로 추가
        System.out.println(member);

        Member member2 = em.find(Member.class, 1L); // 영속성컨텍스트에 있는 Member를 조회
        System.out.println(member2);

        //복잡한 조건 조회 쿼리
        TypedQuery<Member> query = em.createQuery("SELECT m FROM Member AS m WHERE m.email LIKE :key" , Member.class);
        query.setParameter("key","%user%");
        Member member3 = query.getSingleResult();
        member3.setUserNm("(수정)사용자01");
        em.flush();
    }

    @Test
    void test1(){
        Member member = new Member();
       // member.setUserNo(1L);
        member.setEmail("user01@test.org");
        member.setUserNm("사용자01");
        member.setPassword("123456");
        member.setMobile("01000000000");
        member.setMtype(MemberType.USER);

        //영속성 컨텍스트 안에다가 넣어주는 메서드(persist)
        em.persist(member);
        //db에 추가
        //insert쿼리 수행
        em.flush();
        
        //detach : 영속성 분리 변화 감지 X -> 즉 쿼리 수행을 안함
        em.detach(member);

        //update쿼리 수행
        member.setUserNm("(수정)사용자01");
        em.flush();

        //분리된 영속 상태 되돌리기 -> 변화감지 O
        em.merge(member);
        em.flush();

        //delete쿼리 수행
//        em.remove(member);
//        em.flush();
    }

    @Test
    void test3(){

    }
}
