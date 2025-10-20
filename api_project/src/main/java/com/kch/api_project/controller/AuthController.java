package com.kch.api_project.controller;

import com.kch.api_project.dto.*;
import com.kch.api_project.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "사용자 회원가입을 수행합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        authService.register(registerRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(null, "회원가입이 완료되었습니다."));
    }

    @Operation(summary = "로그인", description = "아이디/비밀번호 검증 후 Access/Refresh Token을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        LoginResponseDTO result = authService.login(loginRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(result, "로그인 성공"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshResponseDTO>> refresh(@RequestBody RefreshRequestDTO req) {
        RefreshResponseDTO response = authService.refreshAccessToken(req);
        return ResponseEntity.ok(ApiResponse.ok(response, "Access Token이 재발급되었습니다."));
    }
}
