package com.board.configs;

import com.board.models.member.MemberInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        String email = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth !=null && auth.getPrincipal() instanceof MemberInfo){
            MemberInfo memberinfo = (MemberInfo) auth.getPrincipal();
            email = memberinfo.getEmail();
         }
        return Optional.ofNullable(email);
        //로그인한 회원 정보가 테이블에 자동 추가
    }
}
