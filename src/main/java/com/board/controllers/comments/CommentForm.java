package com.board.controllers.comments;

import com.board.entities.BoardData;
import com.board.entities.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentForm {
    private Long seq; //게시글 등록 번호
    private Long boardDataSeq; //게시글 번호

    @NotBlank
    private String poster;
    @Size(min = 4)
    private String guestPw;
    @NotBlank
    private String content;

    private Member member;
    private BoardData boardData;

}
