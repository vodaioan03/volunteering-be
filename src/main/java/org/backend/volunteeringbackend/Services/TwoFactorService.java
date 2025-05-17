package org.backend.volunteeringbackend.Services;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.backend.volunteeringbackend.DTO.TwoFactorSetupDTO;
import org.backend.volunteeringbackend.Models.User;
import org.backend.volunteeringbackend.Repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class TwoFactorService {
    private final UserRepository userRepository;
    private final SecretGenerator secretGenerator;
    private final QrGenerator qrGenerator;
    private final CodeVerifier codeVerifier;
    private final TimeProvider timeProvider;

    public TwoFactorService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.secretGenerator = new DefaultSecretGenerator();
        this.qrGenerator = new ZxingPngQrGenerator();
        this.timeProvider = new SystemTimeProvider();
        this.codeVerifier = new DefaultCodeVerifier(new DefaultCodeGenerator(),
                                                   timeProvider);
    }

    @Transactional
    public TwoFactorSetupDTO generateSecret() {
        User user = getCurrentUser();
        String secret = secretGenerator.generate();

        QrData qrData = new QrData.Builder()
                .label(user.getEmail())
                .secret(secret)
                .issuer("Volunteering App")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        try {
            byte[] qrCodeImage = qrGenerator.generate(qrData);
            String qrCodeImageUri = getDataUriForImage(qrCodeImage, qrGenerator.getImageMimeType());

            return TwoFactorSetupDTO.builder()
                    .secret(secret)
                    .qrCodeImage(qrCodeImageUri)
                    .build();
        } catch (QrGenerationException e) {
            throw new RuntimeException("Error generating QR code", e);
        }
    }

    @Transactional
    public boolean verifyAndEnable(String code, String secret) {
        if (verifyCode(secret, code)) {
            User user = getCurrentUser();
            user.setTwoFactorSecret(secret);
            user.setTwoFactorEnabled(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean verifyCode(String code) {
        User user = getCurrentUser();
        if (!user.isTwoFactorEnabled()) {
            throw new IllegalStateException("2FA is not enabled for this user");
        }
        return verifyCode(user.getTwoFactorSecret(), code);
    }

    @Transactional
    public void disable() {
        User user = getCurrentUser();
        user.setTwoFactorSecret(null);
        user.setTwoFactorEnabled(false);
        userRepository.save(user);
    }

    private boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
} 