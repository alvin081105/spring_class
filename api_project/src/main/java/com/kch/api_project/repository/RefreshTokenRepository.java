package com.kch.api_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kch.api_project.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
