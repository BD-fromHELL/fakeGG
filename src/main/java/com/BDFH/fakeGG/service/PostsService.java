package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.PostsResponseDTO;
import com.BDFH.fakeGG.dto.PostPostsRequestDTO;
import com.BDFH.fakeGG.entity.Posts;
import com.BDFH.fakeGG.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    /**
     * 게시글 목록 : 전체 게시글을 10개씩 끊어서 return. List로 return
     */
    @Transactional(readOnly = true)
    public List<PostsResponseDTO> findAll(Pageable pageable){
        Page<Posts> entity = postsRepository.findAll(pageable);
        return entity.stream()
                .map(PostsResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 작성
     */
    @Transactional
    public Posts postPosts(PostPostsRequestDTO request){
        Posts posts = Posts.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .build();
        return postsRepository.save(posts);
    }
}
