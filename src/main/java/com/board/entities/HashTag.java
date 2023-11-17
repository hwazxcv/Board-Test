package com.board.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class HashTag {

    @Id
    @Column(length = 45)
    private String tag;

    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    private List<BoardData> items = new ArrayList<>();



}
