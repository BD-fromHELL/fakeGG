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

    private String contents;

    @OneToMany(mappedBy = "parents",cascade = CascadeType.ALL)
    private List<Comment> childComments;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parents;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

}
