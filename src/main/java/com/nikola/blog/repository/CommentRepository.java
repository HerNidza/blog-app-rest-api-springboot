package com.nikola.blog.repository;

import com.nikola.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(long postId);
    @Query("SELECT c FROM Comment c WHERE c.name LIKE CONCAT('%',:query,'%') ")
    List<Comment> searchByName(String query);
}
