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

//공통적인 속성 추상 클래스
@MappedSuperclass
@Getter
@Setter
//활성화 시켜주기위해 MVCConfig에서 설정
@EntityListeners(AuditingEntityListener.class)
public abstract class Base {
    @Column(updatable = false)

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(insertable = false)

    @LastModifiedDate
    private LocalDateTime modifiedAt;
    
}
