package org.backend.volunteeringbackend.Services;

import jakarta.transaction.Transactional;
import org.backend.volunteeringbackend.Repository.IApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class ApplicationService {
    @Autowired
    private final IApplicationRepository applicationRepository;

    public ApplicationService(IApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public boolean hasUserApplied(UUID opportunityId, UUID userId) {
        return applicationRepository.existsByOpportunityIdAndUserId(opportunityId, userId);
    }

}
