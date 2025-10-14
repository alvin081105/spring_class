package com.kch.api_project.controller;

import com.kch.api_project.dto.RegisterRequestDTO;
import com.kch.api_project.dto.ApiResponse;
import com.kch.api_project.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        authService.register(registerRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok("회원가입이 완료되었습니다."));
    }
}
