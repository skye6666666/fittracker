package com.skye.fittracker.dto;

import com.skye.fittracker.enums.Role;

import java.time.LocalDateTime;

public record UserProfileResponse(
        String username,
        String email,
        Role role,
        LocalDateTime createdAt
) {}
