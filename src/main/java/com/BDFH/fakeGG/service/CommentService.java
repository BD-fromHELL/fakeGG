package com.BDFH.fakeGG.service;

import com.BDFH.fakeGG.dto.CommentResponseDto;
import com.BDFH.fakeGG.dto.PostCommentRequestDto;
import com.BDFH.fakeGG.entity.Article;
import com.BDFH.fakeGG.entity.Comment;
import com.BDFH.fakeGG.entity.Member;
import com.BDFH.fakeGG.exception.NotFoundArticleException;
import com.BDFH.fakeGG.repository.ArticleRepository;
import com.BDFH.fakeGG.repository.CommentRepository;
import com.BDFH.fakeGG.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
// 댓글 대댓글 수정
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public CommentResponseDto postComment(PostCommentRequestDto request){
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new NotFoundArticleException("게시글이 없다라.. 이상하군"));;

        Member user = memberRepository.findByMemberName(request.getMemberName())
                .orElseThrow(() -> new NullPointerException("널널~하네"));
        Comment comment;

        if(request.getParentsId() != null){
            Comment parents = commentRepository.findById(request.getParentsId())
                    .orElseThrow(() -> new NullPointerException());
            comment = Comment.builder()
                    .writer(user)
                    .contents(request.getContents())
                    .article(article)
                    .parents(parents)
                    .build();
        } else {
            comment = Comment.builder()
                .writer(user)
                .contents(request.getContents())
                .article(article)
                .build();
        }

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public Comment deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NullPointerException("널널~하네"));
        commentRepository.deleteById(commentId);

        return comment;
    }

    public List<CommentResponseDto> getComments(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
