package com.BDFH.fakeGG.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class MemberResponseDto {
    private final String email;
    private final String name;
    private final Long birth;
}
