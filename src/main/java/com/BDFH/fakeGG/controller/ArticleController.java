package com.BDFH.fakeGG.controller;



import com.BDFH.fakeGG.dto.ArticleResponseDto;
import com.BDFH.fakeGG.dto.PostArticleRequestDto;
import com.BDFH.fakeGG.entity.Article;
import com.BDFH.fakeGG.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    /**
     * 게시글 목록 : 모든 게시물을 10개씩 끊어서 return.
     * 사용법 : 원하는 page 번호만 쿼리형식으로 보내주면 됨
     */

    @GetMapping("/article/{page}")
    public ArticleResponseDto articleList(@PathVariable int page){
        Pageable pageable = PageRequest.of(page-1, 10);
        ArticleResponseDto articles = articleService.findArticles(pageable);
        return articles;
    }

    @GetMapping("/article/detail/{articleId}")
    public Article getArticleDetail(@PathVariable Long articleId){
        Article article = articleService.getArticleDetail(articleId);
        return article;
    }

    /**
     * 게시글 작성
     */
    @PostMapping("/article")
    private Article postArticle(@RequestBody PostArticleRequestDto request){ // Article => ARticleResponseDto로 바꿩햐ㅏㅁ
        return articleService.postArticle(request);
    }


    /**
     * 게시글 지우기 : id에 해당하는 게시글을 삭제
     */
    @DeleteMapping("/article/{articleId}")
    public String deleteArticle(@PathVariable Long articleId){
            articleService.deleteArticle(articleId);
        return "삭제완료";
    }


}
