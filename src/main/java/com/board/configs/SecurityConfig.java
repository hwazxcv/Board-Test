package com.board.configs;

import com.board.models.member.LoginFailureHandler;
import com.board.models.member.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//시큐리티 적용 설정
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //post로 넘어갈 페이지를 입력
        //알려줘야할 이름값 입력
        http.formLogin(f->{
            f.loginPage("/member/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    //성공하면 메인 페이지 실패하면 로그인 페이지(AuthenticationFailureHandler,AuthenticationSuccessHandler 인터페이스) -> models.member에 만들어둠
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailureHandler());
        });//DSL
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
