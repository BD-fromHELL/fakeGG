package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.ArticleResponseDTO;
import com.BDFH.fakeGG.dto.PostArticleRequestDTO;
import com.BDFH.fakeGG.entity.Article;
import com.BDFH.fakeGG.exception.NotFoundPostsException;
import com.BDFH.fakeGG.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    /**
     * 게시글 목록 : 전체 게시글을 10개씩 끊어서 return. List로 return
     */
    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> findAll(Pageable pageable) {
        Page<Article> entity = articleRepository.findAll(pageable);
        return entity.stream()
                .map(ArticleResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 작성
     */
    @Transactional
    public Article postArticle(PostArticleRequestDTO request) {
        Article article = Article.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .build();
        return articleRepository.save(article);
    }

    /**
     * 게시글 삭제 : articleId에 해당하는 게시글을 삭제한다
     */
    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundPostsException("게시글이 존재하지 않습니다"));
        articleRepository.delete(article);
    }
}
