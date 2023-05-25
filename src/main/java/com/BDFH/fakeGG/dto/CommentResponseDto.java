package com.BDFH.fakeGG.dto;

import com.BDFH.fakeGG.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String username;
    private String contents;
    private Long parentId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getWriter().getName();
        this.contents = comment.getContents();
        this.parentId = comment.getParents().getId();
    }
}
