package com.oie.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank(message="Username should not be blank")
        String username,
        @NotBlank(message="Password should not be blank")
        String password
) {}
