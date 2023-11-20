package com.board.entities;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMember extends Base{

    @CreatedBy
    @Column(length = 65,updatable = false)
    private String createBy; //생성한 사람(email)
    @LastModifiedDate
    @Column(length = 65,insertable = false)
    private String modifiedBy;




}
