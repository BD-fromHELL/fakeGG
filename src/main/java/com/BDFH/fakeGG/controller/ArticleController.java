package com.BDFH.fakeGG.controller;



import com.BDFH.fakeGG.dto.ArticleResponseDto;
import com.BDFH.fakeGG.dto.PostArticleRequestDto;
import com.BDFH.fakeGG.entity.Article;
import com.BDFH.fakeGG.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService postsService;

    /**
     * 게시글 목록 : 모든 게시물을 10개씩 끊어서 return.
     * 사용법 : /posts?size=원하는size&page=원하는page
     */
    @GetMapping("/posts")
    public ArticleResponseDto postsList(@PageableDefault(size=1) Pageable pageable){
        ArticleResponseDto articles = postsService.findArticles(pageable);
        return articles;
    }


    /**
     * 게시글 작성
     */
    @PostMapping("/posts")
    private Article postPosts(@RequestBody PostArticleRequestDto request){
        return postsService.postArticle(request);
    }


    /**
     * 게시글 지우기 : id에 해당하는 게시글을 삭제
     */
    @DeleteMapping("/posts/{postsId}")
    public String deletePosts(@PathVariable Long postsId){
            postsService.deleteArticle(postsId);
        return "삭제완료";
    }


}
