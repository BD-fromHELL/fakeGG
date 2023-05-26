package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleId(Long articleId);
    List<Comment> findByArticleIdAndParentCommentIdIsNull(Long articleId);
}
