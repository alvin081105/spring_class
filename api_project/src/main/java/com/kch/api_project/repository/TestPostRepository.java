package com.kch.api_project.repository;

import com.kch.api_project.entity.TestPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestPostRepository extends JpaRepository<TestPost,Long> {

}