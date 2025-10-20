package com.kch.api_project.service;

import com.kch.api_project.dto.*;
import com.kch.api_project.excepstions.RefreshTokenExpiredException;
import com.kch.api_project.excepstions.TokenNotValidatedException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kch.api_project.entity.RefreshToken;
import com.kch.api_project.entity.Users;
import com.kch.api_project.excepstions.UserAlreadyExistException;
import com.kch.api_project.global.TokenProvider;
import com.kch.api_project.repository.AuthRepository;
import com.kch.api_project.repository.RefreshTokenRepository;
import com.kch.api_project.dto.RefreshRequestDTO;

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
                .expiryDateTime(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(rt);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public RefreshResponseDTO refreshAccessToken(RefreshRequestDTO dto){
        // 1. 사용자의 RefreshToken validation (JWT검중)
        if(!tokenProvider.validateToken(dto.getRefreshToken())){
            //검증 실패 시 대응
            throw new TokenNotValidatedException("토큰 검증에 실패했습니다.");
        }

        // 2. 우리 DB에 사용자가 전달한 RefreshToken이 있는지 확인
        // 찾을수 없는 경우 프론트가 로그아웃 시킬수 있도록 처리
        RefreshToken existingToken = refreshTokenRepository.findByRefreshToken(dto.getRefreshToken())
                .orElseThrow(() -> new RefreshTokenExpiredException("토큰을 찾을 수 없습니다."));

        // 3. 있으면, expiredDate가 지나진 않았느닞 확인
        if (existingToken.getExpiryDateTime().isBefore(LocalDateTime.now())){
            // 만료된 경우 프론트가 로그아웃 시킬수 있도록 처리
            throw new RefreshTokenExpiredException("토큰이 만료되었습니다.");
        }

        // 4. 검증 성공 시, AccessToken 생성 후 반환
        String username = tokenProvider.getUsername((existingToken.getRefreshToken()));
        String newAccessToken = tokenProvider.createAccessToken(username);

        return RefreshResponseDTO.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
