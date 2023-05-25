package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByMemberName(String memberName);
}
