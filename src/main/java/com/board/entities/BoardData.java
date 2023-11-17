package com.board.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardData extends BaseMember{
    @Id
    @GeneratedValue
    private Long seq;
    @Column(length = 50 ,nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime regDt;
    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime modDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private Member member;

    @ManyToMany(fetch=FetchType.LAZY)
    private List<HashTag> tags = new ArrayList<>();

}
