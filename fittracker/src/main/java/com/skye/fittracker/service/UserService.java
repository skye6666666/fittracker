package com.skye.fittracker.service;

import com.skye.fittracker.dto.*;
import com.skye.fittracker.entity.User;
import com.skye.fittracker.entity.VerificationToken;
import com.skye.fittracker.enums.Role;
import com.skye.fittracker.util.JwtUtil;
import com.skye.fittracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.skye.fittracker.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, VerificationTokenRepository verificationTokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
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
        user.setRole(Role.USER);
        user.setEnabled(true);

        User saved = userRepository.save(user);

//因上線雲端無法寄送email，因此先拿掉註冊寄驗證信功能
//        String token = UUID.randomUUID().toString();
//
//        VerificationToken verificationToken = new VerificationToken();
//
//        verificationToken.setToken(token);
//        verificationToken.setUser(saved);
//
//        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));

//        verificationTokenRepository.save(verificationToken);

//
//        emailService.sendVerificationEmail(
//                saved.getEmail(),
//                token
//        );

        return new UserResponse(saved.getId(), saved.getUsername(),
                saved.getEmail(), saved.getCreatedAt());
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.isEnabled()){
            throw new RuntimeException("Please verify your email");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(),user.getRole());


        return new LoginResponse(token);
    }

    public UserProfileResponse getMyProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        return new UserProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {

        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

//        if(request.getNewPassword().length() < 6){
//            throw new RuntimeException(
//                    "Password must be at least 6 characters");
//        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException(
                    "Passwords do not match");
        }

        if(passwordEncoder.matches(request.getNewPassword(),user.getPassword())) {
            throw new RuntimeException(
                    "New password cannot be same as current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

    }

    public void verifyEmail(String token) {

        VerificationToken verificationToken =
                verificationTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid token"));

        if (verificationToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("Token expired");
        }

        User user =
                verificationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);
    }

}