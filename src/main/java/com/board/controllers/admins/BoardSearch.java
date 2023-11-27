package com.board.controllers.admins;



import lombok.Data;

//전달용 객체

@Data
public class BoardSearch {

    private int page = 1 ; //페이지 기본값
    private int limit= 10; //페이지당 보여질 페이지 개수
}
