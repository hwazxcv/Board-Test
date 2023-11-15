package com.board.repositories;

import com.board.entities.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {


    Optional<Member> findByEmail(String email);
}
