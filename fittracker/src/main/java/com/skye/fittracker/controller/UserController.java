package com.skye.fittracker.controller;

import com.skye.fittracker.dto.LoginRequest;
import com.skye.fittracker.dto.LoginResponse;
import com.skye.fittracker.dto.UserRegisterRequest;
import com.skye.fittracker.dto.UserResponse;
import com.skye.fittracker.entity.User;
import com.skye.fittracker.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}