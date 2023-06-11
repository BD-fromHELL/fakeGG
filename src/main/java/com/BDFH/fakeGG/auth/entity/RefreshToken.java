package com.BDFH.fakeGG.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_email", nullable = false, unique = true)
    private String memberEmail;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public RefreshToken(String memberEmail, String refreshToken) {
        this.memberEmail = memberEmail;
        this.refreshToken = refreshToken;
    }

    /**
     * refresh 토큰 업데이트
     */
    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
