package com.kch.api_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
  단일 필드 검증 에러를 담는 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDTO {
    private String field;    // 오류가 발생한 필드명
    private String message;  // 오류 메시지
}
