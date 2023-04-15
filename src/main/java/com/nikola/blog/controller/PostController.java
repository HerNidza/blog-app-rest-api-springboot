package com.nikola.blog.controller;

import com.nikola.blog.payload.PostDto;
import com.nikola.blog.payload.PostDtoV2;
import com.nikola.blog.payload.PostResponse;
import com.nikola.blog.service.PostService;
import com.nikola.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "CRUD REST APIs for Post Resource")
public class PostController {
    private PostService postService;
    private ModelMapper modelMapper;
    @Autowired
    public PostController(PostService thePostService, ModelMapper theModelNapper) {
        postService = thePostService;
        modelMapper = theModelNapper;
    }

    // Blog -> Post REST Api
    // Create -> Post REST Api
    @Operation(summary = "Create Post REST API",description = "Create Post REST API is used to save post into the database")
    @ApiResponse(responseCode = "201",description = "Http Status 201 CREATED")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    // Get All -> Post REST Api
    @Operation(summary = "Get All Posts REST API",description = "Get All Posts REST API is used to fetch all posts from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)int pageNo,
                                    @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,
                                    @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)String sortBy,
                                    @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)String sortDir) {
        return postService.getAllPosts(pageNo, pageSize, sortBy,sortDir);
    }

    // Get Post by ID -> Post REST Api
    @Operation(summary = "Get Post by ID REST API",description = "Get Post by ID REST API is used to get single post from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @GetMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> getPostByIdV1(@PathVariable("id")long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // ****************************************************************************************************************************************
    // Get Post V2 by ID -> Post REST Api -> Versioning
    @Operation(summary = "Get Post V2 by ID REST API",description = "Get Post V2 by ID REST API is used to get single post from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @GetMapping("/api/v2/posts/{id}")
    public ResponseEntity<PostDtoV2> getPostByIdV3(@PathVariable("id")long postId) {
        PostDto postDto = postService.getPostById(postId);
        PostDtoV2 postDtoV2 = modelMapper.map(postDto, PostDtoV2.class);
        List<String> tags = new ArrayList<>();
        tags.add("Java");
        tags.add("Spring Boot");
        tags.add("AWS");
        postDtoV2.setTags(tags);
        return ResponseEntity.ok(postDtoV2);
    }
    // ****************************************************************************************************************************************

    // Update -> Post REST Api
    @Operation(summary = "Update Post REST API",description = "Update Post REST API is used to update a particular post in the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable("id")long id) {
        PostDto postResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    // Delete -> Post REST Api
    @Operation(summary = "Delete Post REST API",description = "Delete Post REST API is used to delete a particular post from the database")
    @ApiResponse(responseCode = "200",description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id")long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity was deleted successfully",HttpStatus.OK);
    }

    // Get post by Category -> Post REST Api
    @GetMapping("/api/v1/posts/category/{categoryId}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable("categoryId") Long categoryId) {
        List<PostDto> postDtos = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/api/v1/posts/search")
    public ResponseEntity<List<PostDto>> searchPostsByTitle(@RequestParam("titleQuery") String query){
        return ResponseEntity.ok(postService.searchPostsByTitle(query));
    }
}
