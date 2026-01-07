package com.Assessment_employability.projects.infrastructure.adapter.in.web;

import com.Assessment_employability.projects.domain.exception.BusinessRuleException;
import com.Assessment_employability.projects.domain.model.User;
import com.Assessment_employability.projects.domain.port.out.UserRepositoryPort;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.AuthResponse;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.LoginRequest;
import com.Assessment_employability.projects.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.Assessment_employability.projects.infrastructure.adapter.out.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Authentication
 * Handles user registration and login
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserRepositoryPort userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessRuleException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Email already exists");
        }

        // Create and save user
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.create(request.username(), request.email(), encodedPassword);
        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(savedUser.getId(), savedUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, savedUser.getId(), savedUser.getUsername(), "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user and returns JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessRuleException("Invalid username or password"));

        // Verify password
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessRuleException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());

        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername(), "Login successful"));
    }
}

