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

        //현재 로그인한 회원 정보를 담고있다.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Object principal = auth.getPrincipal();  // 조건할떄 주의!!! 비회원 - String (문자열) : anonumousUser , 회원 -> UserDetails 구현 객체
        if (auth != null && auth.getPrincipal() instanceof MemberInfo) {
            MemberInfo member = (MemberInfo)auth.getPrincipal();
            email = member.getEmail();
        }


        return Optional.ofNullable(email);
    }
}
