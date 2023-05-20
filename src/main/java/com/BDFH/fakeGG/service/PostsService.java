package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
}
