package org.backend.volunteeringbackend.Repository;

import org.backend.volunteeringbackend.Models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IApplicationRepository extends JpaRepository<Application, UUID> {
    // Changed from existsByOpportunityIdAndApplicantId to existsByOpportunityIdAndUserId
    boolean existsByOpportunityIdAndUserId(UUID opportunityId, UUID userId);

}
