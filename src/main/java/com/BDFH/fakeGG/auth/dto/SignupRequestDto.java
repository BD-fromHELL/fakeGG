package com.BDFH.fakeGG.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Getter
public class SignupRequestDto {
    private String email;
    private String name;
    private String password;
    private Long birth;
}
