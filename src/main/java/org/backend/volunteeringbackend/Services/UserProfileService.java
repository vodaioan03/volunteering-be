package org.backend.volunteeringbackend.Services;

import lombok.RequiredArgsConstructor;
import org.backend.volunteeringbackend.DTO.UserProfileDTO;
import org.backend.volunteeringbackend.Models.User;
import org.backend.volunteeringbackend.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new UsernameNotFoundException("Invalid email provided");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return mapToDTO(user);
    }

    @Transactional
    public UserProfileDTO updateUserProfile(String email, UserProfileDTO profileDTO) {
        if (email == null || email.trim().isEmpty()) {
            throw new UsernameNotFoundException("Invalid email provided");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        updateUserFromDTO(user, profileDTO);
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    private UserProfileDTO mapToDTO(User user) {
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

    private void updateUserFromDTO(User user, UserProfileDTO dto) {
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getBirthDate() != null) {
            user.setBirthDate(dto.getBirthDate());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getAddress() != null) {
            user.setAddress(dto.getAddress());
        }
        if (dto.getCity() != null) {
            user.setCity(dto.getCity());
        }
        if (dto.getState() != null) {
            user.setState(dto.getState());
        }
        if (dto.getCountry() != null) {
            user.setCountry(dto.getCountry());
        }
        if (dto.getZipCode() != null) {
            user.setZipCode(dto.getZipCode());
        }
    }
} 