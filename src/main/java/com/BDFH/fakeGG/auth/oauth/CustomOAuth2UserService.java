package com.BDFH.fakeGG.auth.oauth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final Logger logger = LoggerFactory.getLogger("CustomOAuth2UserService");

    // resource 서버로 access토큰을 보낸 후 받아온 정보로, OAuth2User객체를 생성
    @Override
    public CustomOAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        logger.info("로그인 요청 성공");
        // request로부터 인증된 사용자 객체를 생성
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 생성한 사용자 객체로부터, 어떤 로그인을 사용했는지 가져옴
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 시 키(PK)가 된느 값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        // 소셜 로그인에서 제공하는 유저 정보(Json)
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = null;


        // 카카오 로그인
        if ( registrationId.equals("kakao")) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            String kakao_email = (String) kakaoAccount.get("email");
            email = kakao_email;
        } else
        // 페이스북 로그인
        {
            Map<String, Object> facebookAccount = oAuth2User.getAttributes();
            String facebook_email = (String) facebookAccount.get("email");
            email = facebook_email;
        }

        // CustomOAuth2User를 리턴
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName,
                email);
    }
}
