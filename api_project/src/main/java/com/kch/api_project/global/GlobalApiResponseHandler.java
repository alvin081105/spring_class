package com.kch.api_project.global;

import com.kch.api_project.dto.ApiResponse;
import com.kch.api_project.dto.ValidationErrorDTO;
import com.kch.api_project.excepstions.RefreshTokenExpiredException;
import com.kch.api_project.excepstions.TokenNotValidatedException;
import com.kch.api_project.excepstions.UserAlreadyExistException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
  전역 예외 처리 + 응답 래핑
 */
@RestControllerAdvice
public class GlobalApiResponseHandler {

    /**
      잘못된 리소스 접근 등 (예: 존재하지 않는 게시글)
      404로 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(IllegalArgumentException e) {
        System.out.println("유저가 올바르지 않은 게시글에 접근했습니다.");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("유저가 올바르지 않은 게시글에 접근했습니다."));
    }

    /**
      회원가입 시 이미 존재하는 유저 등
      400으로 응답
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(e.getMessage()));
    }

    /**
      @Valid 검증 실패 처리
      필드별 에러를 리스트로 내려줌
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<ValidationErrorDTO>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        List<ValidationErrorDTO> errorResults = new ArrayList<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorResults.add(
                    ValidationErrorDTO.builder()
                            .field(error.getField())
                            .message(error.getDefaultMessage())
                            .build()
            );
        }

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(errorResults, "잘못된 값이 있습니다."));
    }

    /**
      그 외 예기치 못한 모든 예외
      500으로 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleRefreshTokenExpiredException(RefreshTokenExpiredException e){
        return ResponseEntity
                .status(401)
                .body(ApiResponse.fail(e.getMessage()));
    }


    @ExceptionHandler(TokenNotValidatedException.class)
    public ResponseEntity<ApiResponse<Void>> handleTokenValidatedException(TokenNotValidatedException e){
        return ResponseEntity
                .status(401)
                .body(ApiResponse.fail(e.getMessage()));
    }
}
