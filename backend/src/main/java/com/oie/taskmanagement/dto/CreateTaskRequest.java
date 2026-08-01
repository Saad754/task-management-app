package com.oie.taskmanagement.dto;

import com.oie.taskmanagement.entity.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255)
        String title,
        @Size(max = 1000)
        String description,
        @NotNull(message = "Priority is required")
        TaskPriority priority
) {}