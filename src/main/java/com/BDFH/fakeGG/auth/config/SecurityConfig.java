package com.BDFH.fakeGG.auth.config;

import com.BDFH.fakeGG.auth.jwt.JwtAuthenticationFilter;
import com.BDFH.fakeGG.auth.jwt.TokenProvider;
import com.BDFH.fakeGG.auth.oauth.CustomOAuth2UserService;
import com.BDFH.fakeGG.auth.oauth.OAuth2SuccessHandler;
import com.BDFH.fakeGG.auth.security.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * 스프링 시큐리티 비활성화 : 정적 리소스, h2콘솔창, 회원가입에 대해서 스프링 시큐리티를 비활성화함
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/article/**","/riotApi/**","/static/**", "/accounts/signup", "/auth/login/**", "/newtoken")
                .requestMatchers("/login/oauth2/kakao");

    }


    /**
     * Security Config
     * 기존의 WebSecurityConfigurerAdapter를 extends받는 방식에서
     * SecurityFilterChain을 Bean등록해서 사용하는 방식으로 바뀜
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // cors 사용
                .cors(withDefaults())
                // Rest API를 사용하므로 csrf를 비활성화
                .csrf(csrf -> csrf.disable())
                // 토큰방식을 사용할것이므로, 서버에서 session을 관리하지 않는다는 의미. stateless로 설정함
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 인증 절차에 대한 설정을 시작 : 인증을 통과하면 200, 통과하지 못했다면 403(forbidden)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // requestMatchers() : 특정 url에 대해서 어떻게 인증처리를 할지 결정
                        .requestMatchers("/auth/login/**", "/accounts/signup", "/newtoken").permitAll()
                        // anyRequest() : requestMatchers를 제외한 모든 요청을 의미함
                        // authenticated() : springSecurityContext 내에서 인증이 완료되어야지 접근할 수 있음을 의미
                        .anyRequest().authenticated())
                // oauth2Login을 사용
                .oauth2Login(oauth2Login -> oauth2Login
                        // login시에 사용자 정보를 가져오는 엔드포인트와 서비스를 설정
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        // login 성공시 처리할 핸들러
                        .successHandler(oAuth2SuccessHandler))
                // 폼 로그인 사용 안함, 따라서 UsernamePasswordAuthenticationFilter도 작동하지 않음
                .formLogin(formLogin -> formLogin.disable())
                // 토큰 유효성을 검사하고 authentication을 생성하는 필터를 추가. 필터 추가 순서를 꼭 지켜야한다
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                // 토큰 만료 or 유효하지 않을 시 발생하는 에러를 다루는 필터를 추가
                .addFilterBefore(new ExceptionHandlerFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
                .build();
    }


    /**
     * 암호화 모듈
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
