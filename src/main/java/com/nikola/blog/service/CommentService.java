package com.nikola.blog.service;

import com.nikola.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);
    List<CommentDto> getCommentsByPostId(long postId);
    CommentDto getCommentById(Long postId, Long commentId);
    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto);
    void deleteComment(Long postId, Long commentId);
    List<CommentDto> searchByCommentName(String query);
}
