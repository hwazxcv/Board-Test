package com.board.controllers.boards;


import com.board.entities.FileInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

//요청 커맨트 객체
@Data
public class BoardForm {

    private String mode = "write";
    private Long seq;
    private String bId;
    private String gid = UUID.randomUUID().toString();
    private String category;

    @NotBlank
    private String subject;

    @NotBlank
    private String poster;

    @NotBlank
    private String content;

    private boolean notice;
    private String guestPw; //비회원 체크
    private List<FileInfo> editorImages;
    private List<FileInfo> attachFiles;
}
