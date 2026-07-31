package com.oie.taskmanagement.dto;

import com.oie.taskmanagement.entity.TaskPriority;
import com.oie.taskmanagement.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255)
        String title,
        @Size(max = 1000)
        String description,
        @NotNull(message = "Priority is required")
        TaskPriority priority,
        @NotNull(message = "Status is required")
        TaskStatus status
) {}