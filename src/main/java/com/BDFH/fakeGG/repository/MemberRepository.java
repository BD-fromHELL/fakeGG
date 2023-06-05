package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByMemberEmail(String email);

}

