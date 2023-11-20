package com.board.entities;


import com.board.commons.constants.MemberType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name="idx_member_userNm", columnList = "userNm"),
        @Index(name="idx_member_mobile", columnList = "mobile")
})
public class Member extends Base {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userNo;

    @Column(length=65, unique = true, nullable = false)
    private String email;

    @Column(length=65, name="pw", nullable = false)
    private String password;

    @Column(length=40, nullable = false)
    private String userNm;

    @Column(length=11)
    private String mobile;

    @Column(length=10, nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberType mtype = MemberType.USER; //회원인지 아닌지 판단

    @ToString.Exclude //순환 참조 방지
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private List<BoardData> items = new ArrayList<>();



}
//    @Transient //DB에 반영을 하지않을떄 쓴다.(내부에서만 쓰려는 목적)
//    private String tmpData;







