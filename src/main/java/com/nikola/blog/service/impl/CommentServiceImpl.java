package com.nikola.blog.service.impl;

import com.nikola.blog.entity.Comment;
import com.nikola.blog.entity.Post;
import com.nikola.blog.exception.BlogAPIException;
import com.nikola.blog.exception.ResourceNotFoundException;
import com.nikola.blog.payload.CommentDto;
import com.nikola.blog.repository.CommentRepository;
import com.nikola.blog.repository.PostRepository;
import com.nikola.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    @Autowired
    public CommentServiceImpl(CommentRepository theCommentRepository, PostRepository thePostRepository, ModelMapper theMapper) {
        commentRepository = theCommentRepository;
        postRepository = thePostRepository;
        mapper = theMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        // Retrieve post entity by ID
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // Set post to comment entity
        comment.setPost(post);

        // Comment entity to database
        Comment newComment =  commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // Retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        // Convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // Retrieve post entity by id
        Comment comment = getCommentIdAndPostId(postId, commentId);

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        Comment comment = getCommentIdAndPostId(postId, commentId);

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());
        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }
    @Override
    public void deleteComment(Long postId, Long commentId) {
        // Retrieve post entity by id
        Comment comment = getCommentIdAndPostId(postId, commentId);

        commentRepository.delete(comment);
    }


    // Helpers
    private Comment getCommentIdAndPostId(Long postId, Long commentId) {
        // Retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "ID", postId));

        // Retrieve comment by ID
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","ID", commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post!");
        }
        return comment;
    }
    private CommentDto mapToDTO(Comment comment){
        return mapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto){
        return mapper.map(commentDto, Comment.class);
    }
}




/*
    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
*/
