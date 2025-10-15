package com.kch.api_project.controller;

import com.kch.api_project.dto.LoginRequestDTO;
import com.kch.api_project.dto.LoginResponseDTO;
import com.kch.api_project.dto.RegisterRequestDTO;
import com.kch.api_project.dto.ApiResponse;
import com.kch.api_project.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        authService.register(registerRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(null, "회원가입이 완료되었습니다."));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        LoginResponseDTO result = authService.login(loginRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(result, "로그인 성공"));
    }
}

