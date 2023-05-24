package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.entity.Comment;
import com.BDFH.fakeGG.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

//    @PostMapping("/article/{articleId}/comment")
//    public Comment postComment(@RequestBody Comment){
//
//    }
//
//    @DeleteMapping("/article/{articleId}/comment/{commentId}")
//    public Comment deleteComment(){
//
//    }
//
//    @PostMapping("/article/{articleId}/comment/{commentId}/comment")
//    public Comment postCocoment(){
//
//    }
}
