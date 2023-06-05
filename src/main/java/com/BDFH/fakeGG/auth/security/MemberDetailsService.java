package com.BDFH.fakeGG.auth.security;

import com.BDFH.fakeGG.auth.MemberDetails;
import com.BDFH.fakeGG.entity.Member;
import com.BDFH.fakeGG.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 로그인시 전달받은 email로 유저를 가져옴
     * 이후 가져온 유저로 memberDetails를 생성
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // email을 이용하여 db에 저장된 member를 가져온다. 이 member에는 id와 pw가 들어있다
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException());
        // Member정보를 MemberDetails에 담아준다. 생성자를 이용한다
        return new MemberDetails(member);
    }
}
