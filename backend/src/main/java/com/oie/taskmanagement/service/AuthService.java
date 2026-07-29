package com.oie.taskmanagement.service;

import com.oie.taskmanagement.dto.RegisterRequest;
import com.oie.taskmanagement.dto.UserResponse;
import com.oie.taskmanagement.entity.User;
import com.oie.taskmanagement.exception.DuplicateResourceException;
import com.oie.taskmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already taken");
        } else if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already in use");
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.username(), hashedPassword, request.email());
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail());
    }
}
