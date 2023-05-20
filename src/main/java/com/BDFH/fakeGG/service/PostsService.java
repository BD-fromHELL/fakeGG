package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.PostPostsRequestDTO;
import com.BDFH.fakeGG.entity.Posts;
import com.BDFH.fakeGG.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    public Posts postPosts(PostPostsRequestDTO request){
        Posts posts = Posts.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .build();
        return postsRepository.save(posts);
    }
}
