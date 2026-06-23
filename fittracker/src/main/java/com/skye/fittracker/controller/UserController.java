package com.skye.fittracker.controller;

import com.skye.fittracker.dto.*;
import com.skye.fittracker.entity.User;
import com.skye.fittracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterRequest request) {
        userService.register(request);
        return "Register Success";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(
            @AuthenticationPrincipal User user
    ) {
        return userService.getMyProfile(user.getId());
    }

    @PutMapping("/password")
    public String changePassword(@AuthenticationPrincipal User user,
                                 @Valid @RequestBody ChangePasswordRequest request){
        userService.changePassword(user.getId(), request);
        return "Password changed Successfully!";
    }

}