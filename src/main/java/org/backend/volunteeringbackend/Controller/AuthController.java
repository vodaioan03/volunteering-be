package org.backend.volunteeringbackend.Controller;

import jakarta.validation.Valid;
import org.backend.volunteeringbackend.DTO.AuthResponse;
import org.backend.volunteeringbackend.DTO.LoginRequest;
import org.backend.volunteeringbackend.DTO.RegisterRequest;
import org.backend.volunteeringbackend.DTO.TwoFactorVerifyRequest;
import org.backend.volunteeringbackend.Services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

} 