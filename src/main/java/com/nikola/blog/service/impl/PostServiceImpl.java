package com.nikola.blog.service.impl;

import com.nikola.blog.entity.Category;
import com.nikola.blog.entity.Post;
import com.nikola.blog.exception.ResourceNotFoundException;
import com.nikola.blog.payload.PostDto;
import com.nikola.blog.payload.PostResponse;
import com.nikola.blog.repository.CategoryRepository;
import com.nikola.blog.repository.PostRepository;
import com.nikola.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;
    @Autowired
    public PostServiceImpl(PostRepository thePostRepository,ModelMapper theMapper, CategoryRepository theCategoryRepository) {
        postRepository = thePostRepository;
        mapper = theMapper;
        categoryRepository = theCategoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category","ID",postDto.getCategoryId()));

        // Convert DTO to entity
        Post post = mapToEntity(postDto);
        post.setCategory(category);
        Post newPost = postRepository.save(post);

        // Convert entity to DTO
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Create pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));

        Page<Post> posts = postRepository.findAll(pageable);

        // Get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content =  listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        // Get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","ID",id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {

        // Get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","ID",id));

        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category","ID",postDto.getCategoryId()));


        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        // Get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","ID",id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","ID",categoryId));
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> searchPostsByTitle(String query) {
        List<Post> resultsPostsByTitle = postRepository.searchProductsByTitle(query);
        return resultsPostsByTitle.stream().map(result -> mapToDTO(result)).collect(Collectors.toList());
    }

    //Conversion Entity into DTO
    private PostDto mapToDTO(Post post){
        return mapper.map(post, PostDto.class);
    }
    //Conversion DTO to Entity
    private Post mapToEntity(PostDto postDto){
        return mapper.map(postDto, Post.class);
    }
}



/*

    //Conversion Entity into DTO
    private PostDto mapToDTO(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }

     //Conversion DTO to Entity
    private Post mapToEntity(PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }


*/
