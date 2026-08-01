package com.oie.taskmanagement.repository;

import com.oie.taskmanagement.entity.Task;
import com.oie.taskmanagement.entity.TaskPriority;
import com.oie.taskmanagement.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
    List<Task> findByUserIdAndPriority(Long userId, TaskPriority priority);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
}