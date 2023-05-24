package com.BDFH.fakeGG.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class PostCommentRequestDto {
    private String username;
    private Long articleId;
    private Long parentsId;
    private String contents;
}
