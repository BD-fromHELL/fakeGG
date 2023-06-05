package com.BDFH.fakeGG.auth.config;

import com.BDFH.fakeGG.auth.jwt.JwtAuthenticationFilter;
import com.BDFH.fakeGG.auth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final TokenProvider tokenProvider;

    /**
     * 스프링 시큐리티 비활성화 : 정적 리소스, h2콘솔창, 회원가입에 대해서 스프링 시큐리티를 비활성화함
     */
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**")
                .requestMatchers("/signup");
    }


    /**
     * Security Config
     *   기존의 WebSecurityConfigurerAdapter를 extends받는 방식에서
     *   SecurityFilterChain을 Bean등록해서 사용하는 방식으로 바뀜
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 일단 cors는 사용하지 않음
                .cors(cors -> cors.disable())
                // Rest API를 사용하므로 csrf를 비활성화
                .csrf(csrf -> csrf.disable())
                // 토큰방식을 사용할것이므로, 서버에서 session을 관리하지 않는다는 의미. stateless로 설정함
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 인증 절차에 대한 설정을 시작 : 인증을 통과하면 200, 통과하지 못했다면 403(forbidden)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // requestMatchers() : 특정 url에 대해서 어덯게 인증처리를 할지 결정
                        .requestMatchers("/login", "/signup").permitAll()
                        // anyRequest() : requestMatchers를 제외한 모든 요청을 의미함
                        // authenticated() : springSecurityContext 내에서 인증이 완료되어야지 접근할 수 있음을 의미
                        .anyRequest().authenticated())
                // 폼 로그인 사용 안함
                .formLogin(formLogin -> formLogin.disable())
                // 토큰 유효성을 검사하고 authentication을 생성하는 필터를 추가
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 암호화 모듈
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
