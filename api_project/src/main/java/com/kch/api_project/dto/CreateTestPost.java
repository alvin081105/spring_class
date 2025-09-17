package com.kch.api_project.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTestPost {
    @Size(min=4)
    private String title;

    @Size(min=10)
    private String body;
}
