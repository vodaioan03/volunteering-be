package org.backend.volunteeringbackend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwoFactorVerifyRequest {
    @NotBlank(message = "Temporary token is required")
    private String temporaryToken;

    @NotBlank(message = "2FA code is required")
    private String code;
} 