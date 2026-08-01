package com.oie.taskmanagement.service;

import com.oie.taskmanagement.dto.LoginRequest;
import com.oie.taskmanagement.dto.LoginResponse;
import com.oie.taskmanagement.dto.RegisterRequest;
import com.oie.taskmanagement.dto.UserResponse;
import com.oie.taskmanagement.entity.User;
import com.oie.taskmanagement.exception.DuplicateResourceException;
import com.oie.taskmanagement.exception.InvalidCredentialsException;
import com.oie.taskmanagement.repository.UserRepository;
import com.oie.taskmanagement.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
        boolean matches = passwordEncoder.matches(request.password(), user.getPassword());
        if (!matches) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        String token = jwtService.generateToken(user.getUsername());
        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail());
        return new LoginResponse(token, userResponse);
    }
}
