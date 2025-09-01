package com.example.AutomatizationQA.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserRequest {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private Boolean status;
    private LocalDateTime lastLogin;
}
