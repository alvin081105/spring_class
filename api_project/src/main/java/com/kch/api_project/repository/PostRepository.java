package com.kch.api_project.repository;

import com.kch.api_project.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.viewCount DESC")
    Page<Post> findByIsDeletedFalseOrderByHitsDesc(Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.created_at DESC")
    Page<Post> findByIsDeletedFalseOrderByCreateDateTimeDesc(Pageable pageable);

    Page<Post> findAllByUserId(Integer userId, Pageable pageable);
}
