package com.BDFH.fakeGG.controller;


import com.BDFH.fakeGG.dto.PostsResponseDTO;
import com.BDFH.fakeGG.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostsController {
    private final PostsService postsService;

    /**
     * 게시글 목록 : 모든 게시물을 10개씩 끊어서 return
     */
    @GetMapping("/posts")
    public List<PostsResponseDTO> postsList(){

        List<PostsResponseDTO> posts = postsService.findAll();
        return posts;
    }

}
