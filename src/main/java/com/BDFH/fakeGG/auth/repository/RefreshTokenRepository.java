package com.BDFH.fakeGG.auth.repository;

import com.BDFH.fakeGG.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberEmail(String email);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
