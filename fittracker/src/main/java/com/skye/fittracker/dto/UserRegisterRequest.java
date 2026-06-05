package com.skye.fittracker.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String email;
    private String password;
}