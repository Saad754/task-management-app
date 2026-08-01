package com.oie.taskmanagement.service;

import com.oie.taskmanagement.dto.CreateTaskRequest;
import com.oie.taskmanagement.dto.TaskResponse;
import com.oie.taskmanagement.dto.UpdateTaskRequest;
import com.oie.taskmanagement.entity.Task;
import com.oie.taskmanagement.entity.TaskPriority;
import com.oie.taskmanagement.entity.TaskStatus;
import com.oie.taskmanagement.entity.User;
import com.oie.taskmanagement.exception.ResourceNotFoundException;
import com.oie.taskmanagement.repository.TaskRepository;
import com.oie.taskmanagement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority());
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        User user = getCurrentUser();
        Task task = new Task(request.title(), request.description(),
                request.priority(), TaskStatus.TODO, user);
        return toResponse(taskRepository.save(task));
    }
    public List<TaskResponse> getTasks(TaskStatus status, TaskPriority priority) {
        User user = getCurrentUser();

        List<Task> tasks;
        if (status != null) {
            tasks = taskRepository.findByUserIdAndStatus(user.getId(), status);
        } else if (priority != null) {
            tasks = taskRepository.findByUserIdAndPriority(user.getId(), priority);
        } else {
            tasks = taskRepository.findByUserId(user.getId());
        }

        List<TaskResponse> responses = new ArrayList<>();
        for (Task task : tasks) {
            responses.add(toResponse(task));
        }
        return responses;
    }
    public TaskResponse getTask(Long id) {
        User user = getCurrentUser();
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return toResponse(task);
    }
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        User user = getCurrentUser();
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setStatus(request.status());
        return toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        User user = getCurrentUser();
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
    }
}