package com.board.repositories;

import com.board.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//JPA레포지토리 상속
public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(String email);
}
