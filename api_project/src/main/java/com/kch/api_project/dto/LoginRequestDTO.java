package com.kch.api_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotBlank
    public String username;
    @NotBlank
    public String password;
}
