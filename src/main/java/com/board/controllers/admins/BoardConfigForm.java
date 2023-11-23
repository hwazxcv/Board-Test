package com.board.controllers.admins;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//게시판에서 들어온 요청데이터 (커맨드 객체)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardConfigForm {

    private String mode;


    @NotBlank(message = "아이디를 입력하세요")
    private String bId;
    @NotBlank(message = "게시판 이름을 입력하세요")
    private String bName;

    private boolean active;

    private String authority ="ALL";

    private String category;

}
