package com.BDFH.fakeGG.controller;



import com.BDFH.fakeGG.dto.ArticleResponseDto;
import com.BDFH.fakeGG.dto.PostArticleRequestDto;
import com.BDFH.fakeGG.entity.Article;
import com.BDFH.fakeGG.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    /**
     * 게시글 목록 : 모든 게시물을 10개씩 끊어서 return
     * 사용법 : /posts?size=원하는size&page=원하는page
     */
    @GetMapping("/article")
    public List<ArticleResponseDto> articleList(Pageable pageable){
        List<ArticleResponseDto> articles = articleService.findAll(pageable);
        return articles;
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
