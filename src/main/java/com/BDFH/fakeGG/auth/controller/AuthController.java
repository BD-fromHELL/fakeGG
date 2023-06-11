package com.BDFH.fakeGG.auth.controller;

import com.BDFH.fakeGG.auth.dto.LoginRequestDto;
import com.BDFH.fakeGG.auth.dto.SignupRequestDto;
import com.BDFH.fakeGG.auth.dto.TokenResponseDto;
import com.BDFH.fakeGG.auth.security.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    /**
     * 로그인 : accessToken, refreshToken을 리턴
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        TokenResponseDto tokenResponseDto = authService.login(loginRequestDto);
        return new ResponseEntity<>(tokenResponseDto, HttpStatus.OK);
    }


    /**
     * 회원 가입 : db에 유저 정보를 저장
     */
    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequestDto signupRequestDto) {
        authService.signUp(signupRequestDto);
    }


    /**
     * 액세스 토큰 재발급 : 액세스 토큰이 만료되었을 때 재발급 받는다
     */
    @GetMapping("/newtoken")
    public ResponseEntity<TokenResponseDto> reGenerate(HttpServletRequest request) {
        TokenResponseDto tokenResponseDto = authService.getNewToken(request);
        return new ResponseEntity<>(tokenResponseDto, HttpStatus.OK);
    }
}
