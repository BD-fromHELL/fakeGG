package com.BDFH.fakeGG.dto;

import com.BDFH.fakeGG.entity.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDTO {
    private Long id;
    private String title;
    private String contents;

    public PostsResponseDTO(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.contents = entity.getContents();
    }
}
