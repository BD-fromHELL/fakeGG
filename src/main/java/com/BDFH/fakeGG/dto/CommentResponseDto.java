package com.BDFH.fakeGG.dto;

import com.BDFH.fakeGG.entity.Comment;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long id;
    private String writer;
    private String contents;

    private int likes;
    private int dislikes;

    private List<CocomentResponseDto> Cocoments;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.writer = comment.getWriter();
        this.contents = comment.getContents();
        this.likes = comment.getLikes();
        this.dislikes = comment.getDislikes();
        this.Cocoments = comment.getChildComments().stream()
                                .map(CocomentResponseDto::new)
                                .collect(Collectors.toList());
    }
}
