package org.backend.volunteeringbackend.Repository;

import org.backend.volunteeringbackend.Models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    @Query("SELECT al.user.id, COUNT(al) FROM AuditLog al " +
           "WHERE al.timestamp >= :startTime " +
           "GROUP BY al.user.id")
    List<Object[]> countUserActionsInTimeWindow(LocalDateTime startTime);
} 