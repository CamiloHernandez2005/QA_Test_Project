package com.example.AutomatizationQA.Controllers;

import com.example.AutomatizationQA.DTOs.GoogleToken;
import com.example.AutomatizationQA.DTOs.LoginRequest;
import com.example.AutomatizationQA.DTOs.LoginResponse;
import com.example.AutomatizationQA.Services.AuthService;
import com.example.AutomatizationQA.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleToken googleTokenDTO) throws Exception {
        String jwt = authService.authenticateWithGoogle(googleTokenDTO);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildJwtCookie(jwt, 12 * 60 * 60).toString())
                .body("Login successful");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildJwtCookie(response.getToken(), 12 * 60 * 60).toString())
                .body("Login successful");    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie logoutCookie = buildJwtCookie("", 0);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, logoutCookie.toString())
                .body("Logout exitoso");
    }

    @GetMapping("/health")
    public String health() {
        return "Health World!";
    }
    private ResponseCookie buildJwtCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from("jwt", value)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(maxAgeSeconds)
                .build();
    }

}
