package com.example.AutomatizationQA.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRequest {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private Boolean status;
    private LocalDateTime lastLogin;
}
