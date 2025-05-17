package org.backend.volunteeringbackend.Services;

import org.backend.volunteeringbackend.DTO.AuthResponse;
import org.backend.volunteeringbackend.DTO.LoginRequest;
import org.backend.volunteeringbackend.DTO.RegisterRequest;
import org.backend.volunteeringbackend.Models.AuditLog;
import org.backend.volunteeringbackend.Models.Role;
import org.backend.volunteeringbackend.Models.User;
import org.backend.volunteeringbackend.Repository.AuditLogRepository;
import org.backend.volunteeringbackend.Repository.UserRepository;
import org.backend.volunteeringbackend.Security.TempTokenService;
import org.backend.volunteeringbackend.config.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TempTokenService tempTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuditLogRepository auditLogRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TempTokenService tempTokenService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tempTokenService = tempTokenService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .twoFactorEnabled(false)
                .build();

        userRepository.save(user);
        
        logAction(user, AuditLog.ActionType.CREATE, "User", user.getId().toString(), "User registration");
        
        var token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .requires2FA(false)
                .temporaryToken(null)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        
        logAction(user, AuditLog.ActionType.LOGIN, "User", user.getId().toString(), "User login");
        
        if (user.isTwoFactorEnabled()) {
            // Generate a temporary token for 2FA verification
            String tempToken = tempTokenService.generateTempToken(user.getEmail());
            return AuthResponse.builder()
                    .token(null)
                    .requires2FA(true)
                    .temporaryToken(tempToken)
                    .build();
        }

        // If 2FA is not enabled, generate and return the main token
        var token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .requires2FA(false)
                .temporaryToken(null)
                .build();
    }

    public AuthResponse verifyTwoFactorAndLogin(String tempToken, String code) {
        // Extract email from temporary token
        String email = tempTokenService.extractEmail(tempToken);
        if (email == null || !tempTokenService.isTokenValid(tempToken, email)) {
            throw new IllegalArgumentException("Invalid or expired temporary token");
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // TODO: Verify the 2FA code here using TwoFactorService
        // For now, we'll assume the code is valid

        // Generate the main JWT token after successful 2FA
        var token = jwtService.generateToken(user);
        logAction(user, AuditLog.ActionType.LOGIN, "User", user.getId().toString(), "2FA verification successful");

        return AuthResponse.builder()
                .token(token)
                .requires2FA(false)
                .temporaryToken(null)
                .build();
    }

    private void logAction(User user, AuditLog.ActionType action, String entityType, String entityId, String details) {
        var auditLog = AuditLog.builder()
                .user(user)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();
        
        auditLogRepository.save(auditLog);
    }
} 