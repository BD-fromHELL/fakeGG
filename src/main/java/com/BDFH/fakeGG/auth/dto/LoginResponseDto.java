package com.BDFH.fakeGG.auth.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
