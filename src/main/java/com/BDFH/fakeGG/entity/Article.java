package com.BDFH.fakeGG.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

//    mappedBy 설정 다시
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    private String title;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

    @OneToMany
    private List<Comment> comments;
}
