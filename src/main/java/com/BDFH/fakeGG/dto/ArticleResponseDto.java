package com.BDFH.fakeGG.dto;

import com.BDFH.fakeGG.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {
    private List<Article> page;
    private int nowPage;
    private int startPage;
    private int endPage;
}
