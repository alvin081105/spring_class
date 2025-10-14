package com.kch.api_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank
    @Length(max = 100)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Length(max = 15)
    private String nickname;

    @NotBlank
    @Email
    private String email;
}
