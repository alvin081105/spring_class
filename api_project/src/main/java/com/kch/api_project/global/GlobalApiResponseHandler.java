package com.kch.api_project.global;


import com.kch.api_project.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.swing.plaf.ActionMapUIResource;

@RestControllerAdvice
public class GlobalApiResponseHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(
            IllegalArgumentException e
    ){
        System.out.println("유저가 올바르지 않는 게시글에 접근했습니다.");
        return ResponseEntity
                .status(404)
                .body(ApiResponse.fail("유저가 올바르지 않은 게시글에 접근했습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e){
        System.out.println(e.getMessage());

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail(e.getMessage()));
    }

}
