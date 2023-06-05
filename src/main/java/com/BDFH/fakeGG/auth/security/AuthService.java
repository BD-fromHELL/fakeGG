package com.BDFH.fakeGG.auth.security;

import com.BDFH.fakeGG.auth.dto.LoginRequestDto;
import com.BDFH.fakeGG.auth.dto.LoginResponseDto;
import com.BDFH.fakeGG.auth.dto.SignupRequestDto;
import com.BDFH.fakeGG.auth.jwt.RefreshTokenProvider;
import com.BDFH.fakeGG.auth.jwt.TokenProvider;
import com.BDFH.fakeGG.entity.Member;
import com.BDFH.fakeGG.exception.AlreadyMemberExistException;
import com.BDFH.fakeGG.exception.NotExistMemberException;
import com.BDFH.fakeGG.exception.WrongPasswordException;
import com.BDFH.fakeGG.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;

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
     * 로그인을 성공하면 토큰 발급
     */
    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
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
        return new LoginResponseDto(accessToken, refreshToken);
    }
}
