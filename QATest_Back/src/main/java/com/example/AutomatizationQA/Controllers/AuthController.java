package com.example.AutomatizationQA.Controllers;

import com.example.AutomatizationQA.DTOs.GoogleToken;
import com.example.AutomatizationQA.DTOs.LoginRequest;
import com.example.AutomatizationQA.DTOs.LoginResponse;
import com.example.AutomatizationQA.Services.AuthService;
import com.example.AutomatizationQA.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/google")
    public ResponseEntity<String> authenticateWithGoogle(@RequestBody GoogleToken googleTokenDTO) throws Exception {
        String jwt = authService.authenticateWithGoogle(googleTokenDTO);
        return ResponseEntity.ok(jwt);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/health")
    public String health() {
        return "Health World!";
    }
}
