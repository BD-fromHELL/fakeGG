package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.PostsResponseDTO;
import com.BDFH.fakeGG.entity.Posts;
import com.BDFH.fakeGG.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
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
    public List<PostsResponseDTO> findAll(){
        List<Posts> entity = postsRepository.findAll();
        return entity.stream()
                .map(PostsResponseDTO::new)
                .collect(Collectors.toList());
    }


}
