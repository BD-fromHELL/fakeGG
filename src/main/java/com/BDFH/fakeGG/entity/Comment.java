package com.BDFH.fakeGG.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long id;

    private String contents;

    @ManyToOne
    private User writer;

    @ManyToOne
    private Article article;

}
