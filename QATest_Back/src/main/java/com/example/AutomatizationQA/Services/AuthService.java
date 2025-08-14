package com.example.AutomatizationQA.Services;

import com.example.AutomatizationQA.Config.JwtService;
import com.example.AutomatizationQA.DTOs.GoogleToken;
import com.example.AutomatizationQA.DTOs.UserRequest;
import com.example.AutomatizationQA.DTOs.UserResponse;
import com.example.AutomatizationQA.Models.User;
import com.example.AutomatizationQA.Repositorys.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleTokenService googleTokenService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String authenticateWithGoogle(GoogleToken googleTokenDTO) throws Exception {
        try {
            GoogleIdToken.Payload payload = googleTokenService.verifyToken(googleTokenDTO.getToken());
            if (payload == null) {
                throw new Exception("Invalid Google token");
            }

            String email = payload.getEmail();
            String fullName = (String) payload.get("name");
            String username = email.split("@")[0];

            Optional<User> existingUserOpt = userRepository.findByEmail(email);

            UserResponse userDTO;
            if (existingUserOpt.isPresent()) {
                userDTO = userService.toResponse(existingUserOpt.get());
            } else {
                UserRequest newUserRequest = UserRequest.builder()
                        .username(username)
                        .email(email)
                        .fullName(fullName)
                        .status(true)
                        .build();

                userDTO = userService.createUser(newUserRequest);
            }

            if (!userDTO.isStatus()) {
                throw new Exception("User is inactive");
            }

            return jwtService.generateToken(userDTO.getUsername());

        } catch (Exception e) {
            throw new Exception("Google authentication failed: " + e.getMessage(), e);
        }
    }
}
