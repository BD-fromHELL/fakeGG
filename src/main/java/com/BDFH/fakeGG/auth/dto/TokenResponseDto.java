package com.BDFH.fakeGG.auth.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
