package com.nikola.blog.repository;

import com.nikola.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryId(Long categoryId);
    @Query("SELECT p FROM Post  p WHERE p.title LIKE CONCAT('%',:query,'%') ")
    List<Post> searchProductsByTitle(String query);
}
