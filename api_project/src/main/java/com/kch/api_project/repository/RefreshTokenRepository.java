package com.kch.api_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kch.api_project.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
