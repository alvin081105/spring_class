package com.kch.api_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TestPostDetailDTO {
    private String title;
    private String body;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
