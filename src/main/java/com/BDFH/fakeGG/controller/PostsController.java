package com.BDFH.fakeGG.controller;


import com.BDFH.fakeGG.dto.PostPostsRequestDTO;
import com.BDFH.fakeGG.entity.Posts;
import com.BDFH.fakeGG.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;
    @PostMapping("/posts")
    private Posts postPosts(@RequestBody PostPostsRequestDTO request){
        return postsService.postPosts(request);
    }

    @DeleteMapping("/")
    private String deletePosts(){
        return "삭제완료~~";
    }
}
