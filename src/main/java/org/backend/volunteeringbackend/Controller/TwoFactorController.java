package org.backend.volunteeringbackend.Controller;

import org.backend.volunteeringbackend.DTO.AuthResponse;
import org.backend.volunteeringbackend.DTO.TwoFactorSetupDTO;
import org.backend.volunteeringbackend.DTO.TwoFactorVerifyDTO;
import org.backend.volunteeringbackend.DTO.TwoFactorVerifyRequest;
import org.backend.volunteeringbackend.Services.AuthenticationService;
import org.backend.volunteeringbackend.Services.TwoFactorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/2fa")
@CrossOrigin(
    originPatterns = "*",
    allowedHeaders = {"Authorization", "Content-Type", "Accept"},
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
               RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD},
    maxAge = 3600
)
public class TwoFactorController {
    private final TwoFactorService twoFactorService;
    private final AuthenticationService authenticationService;

    public TwoFactorController(TwoFactorService twoFactorService, AuthenticationService authenticationService) {
        this.twoFactorService = twoFactorService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/setup")
    public ResponseEntity<TwoFactorSetupDTO> setup() {
        return ResponseEntity.ok(twoFactorService.generateSecret());
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(@RequestBody TwoFactorVerifyDTO verifyDTO) {
        boolean verified = twoFactorService.verifyAndEnable(verifyDTO.getCode(), verifyDTO.getSecret());
        return ResponseEntity.ok(verified);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> verifyLogin(@RequestBody TwoFactorVerifyRequest request) {
        // First verify the 2FA code
        if (!twoFactorService.verifyCode(request.getCode())) {
            throw new IllegalArgumentException("Invalid 2FA code");
        }
        
        // If code is valid, generate the main token
        return ResponseEntity.ok(authenticationService.verifyTwoFactorAndLogin(request.getTemporaryToken(), request.getCode()));
    }

    @PostMapping("/disable")
    public ResponseEntity<Void> disable() {
        twoFactorService.disable();
        return ResponseEntity.ok().build();
    }
} 