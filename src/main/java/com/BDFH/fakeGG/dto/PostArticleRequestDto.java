package com.BDFH.fakeGG.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class PostArticleRequestDto {
    private String writer;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String contents;
}
