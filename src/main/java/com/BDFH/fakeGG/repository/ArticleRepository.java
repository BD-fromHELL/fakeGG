package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * 게시글 목록 : 전체 게시글을 10개씩 끊어서 return
     */
    Page<Article> findAll(Pageable pageable);

}
