package org.backend.volunteeringbackend.Services;

import org.backend.volunteeringbackend.Models.AuditLog;
import org.backend.volunteeringbackend.Models.User;
import org.backend.volunteeringbackend.Repository.AuditLogRepository;
import org.backend.volunteeringbackend.Repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserMonitoringService {
    private static final int SUSPICIOUS_ACTIONS_THRESHOLD = 100;
    private static final int MONITORING_WINDOW_MINUTES = 60;
    
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public UserMonitoringService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void monitorUserActivity() {
        LocalDateTime windowStart = LocalDateTime.now().minusMinutes(MONITORING_WINDOW_MINUTES);
        
        // Get action counts per user in the last hour
        List<Object[]> userActionCounts = auditLogRepository.countUserActionsInTimeWindow(windowStart);
        
        for (Object[] result : userActionCounts) {
            UUID userId = (UUID) result[0];
            Long actionCount = (Long) result[1];
            
            if (actionCount > SUSPICIOUS_ACTIONS_THRESHOLD) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null && !user.isMonitored()) {
                    user.setMonitored(true);
                    userRepository.save(user);
                }
            }
        }
    }
} 