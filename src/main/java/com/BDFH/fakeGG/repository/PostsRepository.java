package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

//    /**
//     * 게시글 목록 : 전체 게시글을 10개씩 끊어서 return
//     */
//    List<Posts> findAll();

}
