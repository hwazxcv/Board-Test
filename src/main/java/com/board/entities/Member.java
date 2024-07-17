package com.board.entities;


import com.board.commons.constants.MemberType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Base{
    @Id
    @GeneratedValue// 자동 증감 번호
    private Long userNo;

    @Column(length = 65,unique = true,nullable = false)
    private String email;

    @Column(length=40, nullable = false, unique = true)
    private String userId;

    @Column(length = 65,nullable = false)
    private String password;
    @Column(length = 40,nullable = false)
    private String userNm;
    @Column(length = 15)
    private String mobile;
    @Column(length = 10,nullable = false)
    private MemberType mtype = MemberType.USER; //회원인지 관리자인지 판단


}
