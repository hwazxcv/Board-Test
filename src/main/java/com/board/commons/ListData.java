package com.board.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//페이지 데이터를 담는 클래스 (편의)
//게시판 목록 과 페이지를 담은 전달 데이터

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListData<T> {
    private List<T> content;
    private Pagination pagination;
}