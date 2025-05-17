package org.backend.volunteeringbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.volunteeringbackend.Models.Gender;
import org.backend.volunteeringbackend.Models.Role;
import org.backend.volunteeringbackend.Models.User;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private Role role;
    private boolean isMonitored;
    private boolean has2FAEnabled;

    public static UserProfileDTO fromUser(User user) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .country(user.getCountry())
                .zipCode(user.getZipCode())
                .role(user.getRole())
                .isMonitored(user.isMonitored())
                .has2FAEnabled(user.isTwoFactorEnabled())
                .build();
    }
} 