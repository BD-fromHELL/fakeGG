package com.BDFH.fakeGG.auth.oauth;

import com.BDFH.fakeGG.auth.dto.TokenResponseDto;
import com.BDFH.fakeGG.auth.security.ExceptionHandlerFilter;
import com.BDFH.fakeGG.auth.jwt.RefreshTokenProvider;
import com.BDFH.fakeGG.auth.jwt.TokenProvider;
import com.BDFH.fakeGG.entity.Member;
import com.BDFH.fakeGG.exception.NotSignedUpMemberException;
import com.BDFH.fakeGG.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    private final OAuth2AuthorizedClientService authorizedClientService;
    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 소셜 로그인된 사용자로부터 email을 가져옴
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String,Object> accountInfo = oAuth2User.getAttribute("kakao_account");
        String email = (String) accountInfo.get("email");
        logger.info("소셜 로그인에 요청에 성공했습니다. 회원가입 여부를 확인합니다");

        // 로그인이 완료되었을 때, 회원가입 된 사용자라면 토큰을 발급해줌
        Optional<Member> member = memberRepository.findByMemberEmail(email);
        if (member.isPresent()) {
            // 토큰 발급
            logger.info("회원가입된 사용자입니다. 로그인이 완료되었습니다.");
            String accessToken = tokenProvider.createToken(email);
            String refreshToken = refreshTokenProvider.createRefreshToken(email);
            TokenResponseDto tokenResponseDto = new TokenResponseDto(accessToken, refreshToken);
            // response body에 토큰 추가
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(tokenResponseDto));
        } else {
            throw new NotSignedUpMemberException();
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
