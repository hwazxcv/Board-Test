package com.board.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardData {
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


}
