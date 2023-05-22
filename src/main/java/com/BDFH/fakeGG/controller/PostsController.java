package com.BDFH.fakeGG.controller;



import com.BDFH.fakeGG.dto.PostPostsRequestDTO;
import com.BDFH.fakeGG.dto.PostsResponseDTO;
import com.BDFH.fakeGG.entity.Posts;
import com.BDFH.fakeGG.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;

    /**
     * 게시글 목록 : 모든 게시물을 10개씩 끊어서 return
     * 사용법 : /posts?size=원하는size&page=원하는page
     */
    @GetMapping("/posts")
    public List<PostsResponseDTO> postsList(Pageable pageable){
        List<PostsResponseDTO> posts = postsService.findAll(pageable);
        return posts;
    }

    /**
     * 게시글 작성
     */
    @PostMapping("/posts")
    private Posts postPosts(@RequestBody PostPostsRequestDTO request){
        return postsService.postPosts(request);
    }


    /**
     * 게시글 지우기 : id에 해당하는 게시글을 삭제
     */
    @DeleteMapping("/posts/{postsId}")
    public String deletePosts(@PathVariable Long postsId){
            postsService.deletePosts(postsId);
        return "삭제완료";
    }


}
