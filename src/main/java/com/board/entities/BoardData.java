package com.board.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardData extends Base{

    @Id
    @GeneratedValue
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member;

    @Column(length = 30, nullable = false)
    private String poster;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String content;




}
