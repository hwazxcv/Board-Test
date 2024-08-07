package com.board.controllers.boards;

import lombok.Data;

@Data
public class BoardDataSearch {
    private String bId;
    private int page = 1;
    private int limit = 20;

    private String category;
    private String sopt; // 검색 옵션
    private String skey; // 검색 키워드
}