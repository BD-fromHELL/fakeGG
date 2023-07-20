package com.BDFH.fakeGG.auth.security;

import com.BDFH.fakeGG.auth.dto.LoginRequestDto;
import com.BDFH.fakeGG.auth.dto.TokenResponseDto;
import com.BDFH.fakeGG.auth.dto.SignupRequestDto;
import com.BDFH.fakeGG.auth.jwt.RefreshTokenProvider;
import com.BDFH.fakeGG.auth.jwt.TokenProvider;
import com.BDFH.fakeGG.entity.Member;
import com.BDFH.fakeGG.exception.AlreadyMemberExistException;
import com.BDFH.fakeGG.exception.NotExistMemberException;
import com.BDFH.fakeGG.exception.WrongPasswordException;
import com.BDFH.fakeGG.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 : memberRepository에 멤버 저장
     */
    @Transactional
    public void signUp(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        // 중복 회원 확인
        if (memberRepository.findByMemberEmail(email).isPresent()) {
            throw new AlreadyMemberExistException("해당 email은 사용중입니다");
        }

        // 비밀번호 암호화
        String encodedPW = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 생성
        Member member = Member.builder()
                .memberEmail(signupRequestDto.getEmail())
                .memberName(signupRequestDto.getName())
                .memberPassword(encodedPW)
                .memberBirth(signupRequestDto.getBirth())
                .build();

        // 회원 저장
        memberRepository.save(member);
    }


    /**
     * 자체 로그인 : 로그인을 성공하면 토큰 발급
     */
    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String pw = loginRequestDto.getPassword();

        // email에 해당하는 member 가져옴
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new NotExistMemberException("회원가입된 email이 존재하지 않습니다"));
        // memberPw와 비교해서 일치하지 않으면 에러 발생
        if (!passwordEncoder.matches(pw, member.getMemberPassword())) {
            throw new WrongPasswordException("비밀번호가 일치하지 않습니다");
        }

        // 토큰을 발급
        String accessToken = tokenProvider.createToken(email);
        String refreshToken = refreshTokenProvider.createRefreshToken(email);
        return new TokenResponseDto(accessToken, refreshToken);
    }


    /**
     * AccessToken 재발급 : access 토큰이 만료되었다면 refresh 토큰을 확인하고 재발급함
     */
    public TokenResponseDto getNewToken(HttpServletRequest request, HttpServletResponse response) {
        // request에서 refreshToken을 추출
        String refreshToken = refreshTokenProvider.getRefreshToken(request);
        // refresh 토큰을 유효성 검사하고, 통과한다면 사용자 email을 추출
        String email = refreshTokenProvider.validateRefreshToken(request, response);
        // 유효성 검사에서 error가 발생하지 않는다면 새로운 토큰을 생성
        String newAccessToken = tokenProvider.createToken(email);

        System.out.println("새로운 토큰이 발급되었습니다");
        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }



}
