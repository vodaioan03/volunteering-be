package org.backend.volunteeringbackend.Controller;

import org.backend.volunteeringbackend.Models.Opportunity;
import org.backend.volunteeringbackend.Services.ApplicationService;
import org.backend.volunteeringbackend.Services.OpportunityService;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/apply")
public class ApplyOpportunityController {

    private final OpportunityService opportunityService;
    private final OpportunityWebSocketController webSocketController;
    private final ApplicationService applicationService;


    public ApplyOpportunityController(OpportunityService opportunityService,
                                      OpportunityWebSocketController webSocketController, ApplicationService applicationService) {
        this.opportunityService = opportunityService;
        this.webSocketController = webSocketController;
        this.applicationService = applicationService;
    }

    @GetMapping("/can-apply/{opportunityId}")
    public ResponseEntity<Map<String, Boolean>> canApply(
            @PathVariable UUID opportunityId,
            @RequestParam UUID userId) {

        // 1. Check if opportunity exists and is still open
        Opportunity opportunity = opportunityService.getById(opportunityId);

        // 2. Check if user has already applied (you'll need to implement this)
        boolean hasApplied = applicationService.hasUserApplied(opportunityId, userId);

        // 3. Check other business rules (dates, requirements, etc.)

        boolean isOpen = LocalDate.parse(opportunity.getEndDate()).isAfter(LocalDate.now());

        // 4. Combine all checks
        boolean canApply = isOpen && !hasApplied; // Add other conditions as needed

        return ResponseEntity.ok(Collections.singletonMap("canApply", canApply));
    }
}
