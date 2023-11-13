package com.board.models.member;

import com.board.entities.Member;
import com.board.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {
    private final MemberRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //email(Optional)에 대한 null 값을 처리;
        Member member =  repository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .build();
    }
}
