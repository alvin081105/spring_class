package com.kch.api_project.service;

import com.kch.api_project.dto.RegisterRequestDTO;
import com.kch.api_project.entity.Users;
import com.kch.api_project.repository.AuthRepository;
import com.kch.api_project.excepstions.UserAlreadyExistException;
import com.kch.api_project.global.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequestDTO dto) {
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistException("유저가 이미 존재합니다.");
        }

        Users user = Users.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        authRepository.save(user);
    }
}
