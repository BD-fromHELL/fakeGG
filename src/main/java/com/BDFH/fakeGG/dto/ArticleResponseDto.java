package com.BDFH.fakeGG.dto;

import com.BDFH.fakeGG.entity.Article;
import lombok.Getter;

@Getter
public class ArticleResponseDto {
    private Long id;
    private String title;
    private String contents;

    public ArticleResponseDto(Article entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.contents = entity.getContents();
    }
}
