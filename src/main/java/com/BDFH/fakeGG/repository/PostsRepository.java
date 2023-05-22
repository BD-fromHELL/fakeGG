package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    /**
     * 게시글 목록 : 전체 게시글을 10개씩 끊어서 return
     */
    Page<Posts> findAll(Pageable pageable);

}
