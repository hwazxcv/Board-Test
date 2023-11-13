package com.board.models.member;

import com.board.entities.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder

public class MemberInfo implements UserDetails {

    private String email;
    private String password;
    private Member member;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    //권한에 대한 통제
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    //로그인
    public String getPassword() {
        return password;
    }

    @Override
    //로그인
    public String getUsername() {
        return email;
    }

    @Override
    //만료
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    //잠금
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    //비번 만료
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    //회원 탈퇴 여부
    public boolean isEnabled() {
        return true;
    }
}
