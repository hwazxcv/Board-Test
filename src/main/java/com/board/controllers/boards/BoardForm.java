package com.board.controllers.boards;


import com.board.entities.FileInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

//요청 커맨트 객체
@Data
public class BoardForm {
    private String bId;
    private String mode = "write";
    private Long seq;
    private String gid = UUID.randomUUID().toString();

    private String category;

    @NotBlank(message = "제목을 입력 하세요")
    private String subject;
    @NotBlank(message = "작성자를 입력하세요")
    private String poster;
    @NotBlank(message = "내용을 입력하세요")
    private String content;

    private boolean notice;

    private String guestPw; //비회원 체크

    private List<FileInfo> editorImages;
    private List<FileInfo> attachFile;
}
