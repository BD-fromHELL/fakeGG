package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.dto.MemberResponseDto;
import com.BDFH.fakeGG.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 로그인 되어있는 유저 정보 가져오기
     */
    @GetMapping("/memberinfo")
    public MemberResponseDto getInfo(Authentication authentication) {
        MemberResponseDto memberInfo = memberService.getMemberInfo(authentication);
        return memberInfo;
    }
}
