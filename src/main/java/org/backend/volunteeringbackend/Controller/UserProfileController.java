package org.backend.volunteeringbackend.Controller;

import lombok.RequiredArgsConstructor;
import org.backend.volunteeringbackend.DTO.ErrorResponse;
import org.backend.volunteeringbackend.DTO.UserProfileDTO;
import org.backend.volunteeringbackend.Services.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(
    originPatterns = "*",
    allowedHeaders = {"Authorization", "Content-Type", "Accept"},
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
               RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD},
    maxAge = 3600
)
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                      Authentication authentication) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body(ErrorResponse.of(401, "No authentication token found"));
            }

            if (authentication == null) {
                return ResponseEntity.status(401)
                        .body(ErrorResponse.of(401, "Unauthorized - Please log in again"));
            }

            UserProfileDTO profile = userProfileService.getUserProfile(authentication.getName());
            return ResponseEntity.ok(profile);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(ErrorResponse.of(401, "Unauthorized - Please log in again"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ErrorResponse.of(500, "Failed to fetch profile data: " + e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody UserProfileDTO profileDTO,
            Authentication authentication) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body(ErrorResponse.of(401, "No authentication token found"));
            }

            if (authentication == null) {
                return ResponseEntity.status(401)
                        .body(ErrorResponse.of(401, "Unauthorized - Please log in again"));
            }

            UserProfileDTO updatedProfile = userProfileService.updateUserProfile(authentication.getName(), profileDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(ErrorResponse.of(401, "Unauthorized - Please log in again"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ErrorResponse.of(500, e.getMessage()));
        }
    }
} 