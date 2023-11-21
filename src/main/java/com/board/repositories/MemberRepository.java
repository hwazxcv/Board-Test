package com.board.repositories;

import com.board.entities.Member;
import com.board.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , QuerydslPredicateExecutor<Member> {


    Optional<Member> findByEmail(String email);

    //완성된 메서드로 이용
    default boolean exists(String email) {
        return exists(QMember.member.email.eq(email));


    }
}




