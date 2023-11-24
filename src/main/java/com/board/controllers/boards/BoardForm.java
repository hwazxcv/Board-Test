package com.board.controllers.boards;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
//요청 커맨트 객체
@Data
public class BoardForm {
    private String bId;
    private String mode;
    private Long seq;
    @NotBlank(message = "제목을 입력 하세요")
    private String subject;
    @NotBlank(message = "작성자를 입력하세요")
    private String poster;
    @NotBlank(message = "내용을 입력하세요")
    private String content;

}
