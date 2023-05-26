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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public CommentResponseDto postComment(PostCommentRequestDto request) {
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new NotFoundArticleException("게시글이 없다라.. 이상하군"));;

//        Member user = memberRepository.findByMemberName(request.getMemberName())
//                .orElseThrow(() -> new NullPointerException("널널~하네"));
        Long parentsCommentId = request.getParentsCommentId();

        Comment comment;

        if (parentsCommentId != null) {  // 대댓글이면
            Comment parentsComment = commentRepository.findById(parentsCommentId)
                    .orElseThrow(() -> new NullPointerException("존재하지 않는 댓글인데요"));
            if (parentsComment.getParentComment() != null) {
                new Exception("엥 이건 대댓글인데요");
            }
            comment = Comment.builder()
                    .writer(request.getMemberName())
                    .contents(request.getContents())
                    .likes(0)
                    .dislikes(0)
                    .article(article)
                    .parentComment(parentsComment)
                    .childComments(new ArrayList<Comment>())
                    .build();

            List<Comment> childComments = parentsComment.getChildComments();
            childComments.add(comment);
            commentRepository.save(parentsComment);

        } else {  // 댓글이면
            comment = Comment.builder()
                    .writer(request.getMemberName())
                    .contents(request.getContents())
                    .likes(0)
                    .dislikes(0)
                    .article(article)
                    .childComments(new ArrayList<Comment>())
                    .build();
        }

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NullPointerException("널널~하네"));
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        commentRepository.deleteById(commentId);

        return commentResponseDto;
    }

    public List<CommentResponseDto> getComments(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleIdAndParentCommentIdIsNull(articleId);
        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}