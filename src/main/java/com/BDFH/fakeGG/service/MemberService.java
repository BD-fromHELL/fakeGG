package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.auth.MemberDetails;
import com.BDFH.fakeGG.dto.MemberResponseDto;
import com.BDFH.fakeGG.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 로그인한 사용자의 email, nickname, birth를 return
     */
    public MemberResponseDto getMemberInfo(Authentication authentication){
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        return MemberResponseDto.builder()
                .email(memberDetails.getUseremail())
                .name(memberDetails.getUsername())
                .birth(memberDetails.getUserBirth())
                .build();
    }

}
