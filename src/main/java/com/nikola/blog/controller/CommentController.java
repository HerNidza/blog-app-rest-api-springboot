package com.nikola.blog.controller;

import com.nikola.blog.payload.CommentDto;
import com.nikola.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "CRUD REST APIs for Comment Resource")
public class CommentController {
    private CommentService commentService;
    @Autowired
    public CommentController(CommentService theCommentService) {
        commentService = theCommentService;
    }

    // Blog -> Comment REST Api
    // Create -> Comment REST Api
    @Operation(summary = "Create Comment REST API",description = "Create Post REST API is used to save post into the database")
    @ApiResponse(responseCode = "201",description = "Http Status 201 CREATED")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId,
                                                    @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    // Get All -> Comment REST Api
    @Operation(summary = "Get All Comments REST API",description = "Get All Comments REST API is used to fetch all posts from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") long postId){
        return commentService.getCommentsByPostId(postId);
    }

    // Get Comment by ID -> Post REST Api
    @Operation(summary = "Get Comment by ID REST API",description = "Get Comment by ID REST API is used to get single post from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId")Long postId, @PathVariable("commentId")Long commentId){
        CommentDto commentDto = commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    // Update -> Comment REST Api
    @Operation(summary = "Update Comment REST API",description = "Update Comment REST API is used to update a particular post in the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "commentId") Long commentId,
                                                    @Valid @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(postId,commentId,commentDto);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    // Delete -> Comment REST Api
    @Operation(summary = "Delete Comment REST API",description = "Delete Comment REST API is used to delete a particular post from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId, @PathVariable(value = "commentId") Long commentId){
        commentService.deleteComment(postId, commentId);

        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
    @GetMapping("/posts/{postId}/comments/search")
    public ResponseEntity<List<CommentDto>> searchByCommentName(@RequestParam("queryCommentName") String queryCommentName){
        return ResponseEntity.ok(commentService.searchByCommentName(queryCommentName));
    }
}
