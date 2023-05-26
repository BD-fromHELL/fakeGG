package com.BDFH.fakeGG.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
//    cascade 설정다시
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String writer;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentComment")
    private List<Comment> childComments;

    private int likes;
    private int dislikes;


}
