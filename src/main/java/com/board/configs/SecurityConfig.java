package com.board.configs;

import com.board.models.member.LoginFailureHandler;
import com.board.models.member.LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//시큐리티 적용 설정
@Configuration
@EnableConfigurationProperties(FileUploadConfig.class)
public class SecurityConfig {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //post로 넘어갈 페이지를 입력
        //알려줘야할 이름값 입력
        //로그인 인증 설정
        http.formLogin(f->{
            f.loginPage("/member/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    //성공하면 메인 페이지 실패하면 로그인 페이지(AuthenticationFailureHandler,AuthenticationSuccessHandler 인터페이스) -> models.member에 만들어둠
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailureHandler());
        });//DSL 로그인 인증 설정

        //로그 아웃
        http.logout(c -> {
            c.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/member/login");
        });

        //iframe설정(기본적으로 시큐리티는 막혀있다.)
        http.headers(c->{
           c.frameOptions(o->o.sameOrigin());
        });

        //인가 설정 - 접근 통제
        http.authorizeHttpRequests(c ->{
           c.requestMatchers("/mypage/**").authenticated()// 로그인한 여부 (회원만 접근가능 회원 전용 마이페이지)
            //.requestMatchers("/admin/**").hasAuthority("ADMIN") //관리자 권한만 접근 가능
            .requestMatchers(
                    "/front/css/**",
                    "/front/js/**",
                    "/front/images/**",

                    "/mobile/css/**",
                    "/mobile/js/**",
                    "/mobile/images/**",

                    "/admin/css/**",
                    "/admin/js/**",
                    "/admin/image/**",

                    "/common/css/**",
                    "/common/js/**",
                    "/common/image/**",

                    fileUploadConfig.getUrl()+"**"
            ).permitAll()
            .anyRequest().permitAll(); //나머지 페이지는 권한 필요 없음 X
        });

        http.exceptionHandling(c -> {
            c.authenticationEntryPoint((req,resp,e)->{
                String URI = req.getRequestURI();
                if(URI.indexOf("/admin") != -1 ){ //관리자 페이지 - 401응답 코드
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"NOT AUTHORIZED");

                }else{ // 회원전용 페이지( /mypage) -> 로그인 페이지로 이동
                    String url = req.getContextPath()+"/member/login";
                    resp.sendRedirect(url);
                }
            });
        });


        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
