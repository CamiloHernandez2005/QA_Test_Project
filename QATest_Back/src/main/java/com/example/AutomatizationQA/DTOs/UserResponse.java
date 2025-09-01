package com.example.AutomatizationQA.DTOs;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Boolean status;
}

