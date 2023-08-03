package com.BDFH.fakeGG.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column(columnDefinition = "TEXT")
    private String contents;

    private Long visited;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

}
