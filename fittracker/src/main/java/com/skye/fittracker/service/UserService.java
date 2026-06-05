package com.skye.fittracker.service;

import com.skye.fittracker.dto.LoginRequest;
import com.skye.fittracker.dto.LoginResponse;
import com.skye.fittracker.dto.UserRegisterRequest;
import com.skye.fittracker.dto.UserResponse;
import com.skye.fittracker.entity.User;
import com.skye.fittracker.util.JwtUtil;
import com.skye.fittracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse register(UserRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        //user.setPassword(request.getPassword()); // 之後會改成加密
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getUsername(),
                saved.getEmail(), saved.getCreatedAt());
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(token);
    }
}