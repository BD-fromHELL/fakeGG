package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}
