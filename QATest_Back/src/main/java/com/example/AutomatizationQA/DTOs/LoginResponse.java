package com.example.AutomatizationQA.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String username;
    private String token;
}
