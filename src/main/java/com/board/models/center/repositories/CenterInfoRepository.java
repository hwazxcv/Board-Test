package com.board.models.center.repositories;

import com.board.models.center.entity.CenterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CenterInfoRepository extends JpaRepository<CenterInfo , Long> , QuerydslPredicateExecutor<CenterInfo> {

    List<CenterInfo> findAll();

    Optional<CenterInfo> findBycCode(Long cCode);
}
