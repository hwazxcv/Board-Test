package com.board.controllers.boards;

import com.board.commons.MemberUtil;
import com.board.commons.validators.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class BoardFormValidator  implements Validator, PasswordValidator {

    private final MemberUtil memberUtil;



    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(BoardForm.class);
    }

    //비회원일때 비밀번호 검증(게시판 form 양식 작성중 비회원 일때)

    @Override
    public void validate(Object target, Errors errors) {
        BoardForm form = (BoardForm) target;
        if(!memberUtil.isLogin()){ //미로그인일떄 ->비회원 비밀번호 필수
            String guestPw =form.getGuestPw();
            ValidationUtils.rejectIfEmptyOrWhitespace(errors,"guestPw","NotBlank");

            if(StringUtils.hasText(guestPw)){
            //비 회원 비밀번호는 1자리 이상 알파벳, 숫자 필수
            if(alphaCheck(guestPw, true) || !numberCheck(guestPw)) {
                errors.rejectValue("guestPw", "Complexity");
            }
            //비밀번호는 4자리수 이상
            if(guestPw.length()<4){
                errors.rejectValue("guestpw", "Size");
            }

            }
        }

    }
}
