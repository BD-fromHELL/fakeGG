package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.dto.CommentResponseDto;
import com.BDFH.fakeGG.dto.PostCommentRequestDto;
import com.BDFH.fakeGG.entity.Comment;
import com.BDFH.fakeGG.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /*
    댓글 보여주기
     */

    @GetMapping("/article/{articleId}/comment")
    public List<CommentResponseDto> getComments(@PathVariable Long articleId){
        return commentService.getComments(articleId);
    }

    /*
    댓글 달기
     */
    @PostMapping("/article/{articleId}/comment")
    public CommentResponseDto postComment(@PathVariable Long articleId, @RequestBody PostCommentRequestDto request){
        request.setArticleId(articleId);
        return commentService.postComment(request);
    }


    /*
    댓글 삭제
     */
    @DeleteMapping("/comment/{commentId}")
    public CommentResponseDto deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }


    /*
    대댓글 달기
     */
    @PostMapping("/article/{articleId}/comment/{commentId}/comment")
    public CommentResponseDto postCocoment(@PathVariable Long articleId, @PathVariable Long commentId, @RequestBody PostCommentRequestDto request){
        request.setArticleId(articleId);
        request.setParentsCommentId(commentId);
        return commentService.postComment(request);
    }
}
