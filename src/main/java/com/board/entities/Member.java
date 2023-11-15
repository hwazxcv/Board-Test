package com.board.entities;


import com.board.commons.constants.MemberType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {
        @Index(name="idx_member_userNm",columnList = "userNm"),
        @Index(name="idx_member_mobile", columnList = "mobile")
})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userNo;

    @Column(length = 60,unique = true , nullable = false)
    private String email;

    @Column(length = 60, name="pw" , nullable = false)

    private String password;

    @Column(length = 40,nullable = false)
    private String userNm;

    @Column(length = 15)
    private String mobile;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private MemberType mtype = MemberType.USER; //회원인지 관리자인지 판단

//    @Transient //DB에 반영을 하지않을떄 쓴다.(내부에서만 쓰려는 목적)
//    private String tmpData;


    @Column(updatable = false)
    //추가는 되지만 수정은 안됨
    @CreationTimestamp
    private LocalDateTime regDt;

    @Column(insertable = false)
    //업데이트 될때만 수정
    @UpdateTimestamp
    private LocalDateTime modDt;
}
