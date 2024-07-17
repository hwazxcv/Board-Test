package com.board.models.chatting.entity;


import com.board.entities.Base;
import com.board.entities.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ChatRoom extends Base {


    @Id
    @Column(length = 45)
    private String roomId;

    @Column(length=45, nullable = false)
    private String roomNm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private Member member;

    private int capacity = 2; // 인원수
}
