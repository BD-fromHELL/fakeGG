package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.ArticleResponseDto;
import com.BDFH.fakeGG.dto.PostArticleRequestDto;
import com.BDFH.fakeGG.entity.Article;
import com.BDFH.fakeGG.entity.Comment;
import com.BDFH.fakeGG.entity.Member;
import com.BDFH.fakeGG.exception.NotFoundArticleException;
import com.BDFH.fakeGG.repository.ArticleRepository;
import com.BDFH.fakeGG.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 목록 : 전체 게시글을 10개씩 끊어서 return.
     * 페이지 내비게이션바에 나타낼 현재페이지, 시작페이지, 끝페이지를 같이 전달
     */
    @Transactional(readOnly = true)
    public ArticleResponseDto findArticles(Pageable pageable) {
        Page<Article> entity = articleRepository.findAll(pageable);
        int nowPage = entity.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, entity.getTotalPages());

        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .page(entity.getContent())
                .nowPage(nowPage)
                .startPage(startPage)
                .endPage(endPage)
                .build();
        return articleResponseDto;
    }

    public Article getArticleDetail(Long articleId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundArticleException("게시글이 존재하지 않습니다"));;
        article.setVisited(article.getVisited() + 1);
        articleRepository.save(article);
        return article;
    }
    /**
     * 게시글 작성
     */
    @Transactional
    public Article postArticle(PostArticleRequestDto request) {
        Optional<Member> member = memberRepository.findByMemberEmail(request.getWriter());
        LocalDateTime timestamp = LocalDateTime.now();

        Article article = Article.builder()
                .writer(member.get())
                .title(request.getTitle())
                .contents(request.getContents())
                .timestamp(timestamp)
                .visited(0L)
                .build();
        return articleRepository.save(article);
    }

    /**
     * 게시글 삭제 : articleId에 해당하는 게시글을 삭제한다
     */
    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundArticleException("게시글이 존재하지 않습니다"));
        articleRepository.delete(article);
    }
}
