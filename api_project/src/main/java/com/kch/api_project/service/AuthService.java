package com.kch.api_project.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kch.api_project.dto.LoginResponseDTO;
import com.kch.api_project.dto.LoginRequestDTO;
import com.kch.api_project.dto.RegisterRequestDTO;
import com.kch.api_project.entity.RefreshToken;
import com.kch.api_project.entity.Users;
import com.kch.api_project.excepstions.UserAlreadyExistException;
import com.kch.api_project.global.TokenProvider;
import com.kch.api_project.repository.AuthRepository;
import com.kch.api_project.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    public void register(RegisterRequestDTO dto) {
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistException("유저가 이미 존재합니다.");
        }

        Users user = Users.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(passwordEncoder.encode(dto.getPassword())) // 비밀번호 인코딩
                .build();

        authRepository.save(user);
    }

    // 로그인
    public LoginResponseDTO login(LoginRequestDTO dto) {
        // 사용자 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        // 인증 성공 시 토큰 생성
        String accessToken = tokenProvider.createToken(authentication.getName());
        String refreshToken = tokenProvider.createRefreshToken(authentication.getName());

        // Refresh Token 저장
        RefreshToken rt = RefreshToken.builder()
                .refreshToken(refreshToken)
                .expiry_date_time(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(rt);

        return new LoginResponseDTO(accessToken, refreshToken);
    }
}
