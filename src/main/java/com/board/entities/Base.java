package com.board.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@MappedSuperclass//공통항목으로 사용할 클래스
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) //엔티티 변화감지
public  abstract  class Base {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedAt;
}
