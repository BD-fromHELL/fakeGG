package com.BDFH.fakeGG.auth.controller;

import com.BDFH.fakeGG.auth.MemberDetails;
import com.BDFH.fakeGG.auth.dto.LoginRequestDto;
import com.BDFH.fakeGG.auth.dto.SignupRequestDto;
import com.BDFH.fakeGG.auth.jwt.RefreshTokenProvider;
import com.BDFH.fakeGG.auth.dto.LoginResponseDto;
import com.BDFH.fakeGG.auth.jwt.TokenProvider;
import com.BDFH.fakeGG.auth.security.AuthService;
import com.BDFH.fakeGG.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    /**
     * 로그인 : accessToken, refreshToken을 리턴
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }


    /**
     * 회원 가입 : db에 유저 정보를 저장
     */
    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequestDto signupRequestDto) {
        authService.signUp(signupRequestDto);
    }
}
