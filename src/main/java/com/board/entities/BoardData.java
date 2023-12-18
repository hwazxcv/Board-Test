package com.board.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_bd_data_list", columnList = "notice DESC,createdAt DESC"),
        @Index(name="idx_bd_category" , columnList = "category, notice DESC")
})
public class BoardData extends Base{

    @Id
    @GeneratedValue // 자동 증감 번호
    private Long seq;

    @Column(length =50, nullable = false)
    private String gid = UUID.randomUUID().toString(); //그룹아이디(랜덤 유니크 아이디)

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="bId")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member;

    @Column(length = 50)
    private String category;

    @Column(length = 30, nullable = false)
    private String poster;

    @Column(length = 65)
    private String gusstPw;//비회원 비밀번호

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean notice; // 공지사항 여부

    private int viewCnt; // 조회수

    private int commentCnt; // 댓글 수

    @Transient
    private List<CommentData> comments; // 댓글 목록

    @Transient //디비에 저장하지 않음
    private List<FileInfo> editorImages;

    @Transient
    private List<FileInfo> attachFiles;

}
