package com.nikola.blog.service;

import com.nikola.blog.payload.PostDto;
import com.nikola.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDir);
    PostDto getPostById(long id);
    PostDto updatePost(PostDto postDto, long id);
    void deletePostById(long id);
    List<PostDto> getPostsByCategory(Long categoryId);
    List<PostDto> searchPostsByTitle(String query);
}
