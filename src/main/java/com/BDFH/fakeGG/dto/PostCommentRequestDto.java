package com.BDFH.fakeGG.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class PostCommentRequestDto {
    private String memberName;
    private Long articleId;
    private Long parentsCommentId;
    private String contents;
}
