package com.aiml.fintech.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8) String password
) {}
