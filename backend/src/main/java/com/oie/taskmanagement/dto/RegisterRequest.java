package com.oie.taskmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank
        @Email(message = "Must be a valid email address")
        String email,

        @NotBlank
        @Size(min = 8, max = 64)
        String password
) {}
