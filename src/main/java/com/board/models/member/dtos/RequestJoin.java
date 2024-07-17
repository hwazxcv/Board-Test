package com.board.models.member.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

 //로그인 요청 데이터
    @Data
    public class RequestJoin {


        @NotBlank @Email
        private String email;

        @NotBlank
        @Size(min=8)
        private String password;

        @NotBlank
        private String confirmPassword;

        @NotBlank
        private String userNm;

        private String mobile;

        @AssertTrue
        boolean agree;
    }

