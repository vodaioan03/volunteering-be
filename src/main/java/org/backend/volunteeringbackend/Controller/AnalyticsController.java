package org.backend.volunteeringbackend.Controller;

import org.backend.volunteeringbackend.DTO.AnalyticsSummary;
import org.backend.volunteeringbackend.DTO.MonthlyCount;
import org.backend.volunteeringbackend.DTO.OrganizerCount;
import org.backend.volunteeringbackend.DTO.ViewsDistribution;
import org.backend.volunteeringbackend.Repository.IOpportunityRepository;
import org.backend.volunteeringbackend.Repository.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    @Autowired
    private final IOpportunityRepository opportunityRepository;

    public AnalyticsController(IOpportunityRepository opprepo) {
        this.opportunityRepository = opprepo;
    }
    // 1. Top Organizers Endpoint
    @GetMapping("/top-organizers")
    public ResponseEntity<List<OrganizerCount>> getTopOrganizers(
            @RequestParam(defaultValue = "10") int limit) {

        List<OrganizerCount> results = opportunityRepository.findTopOrganizers(limit);
        return ResponseEntity.ok(results);
    }

    // 2. Monthly Opportunity Counts
    @GetMapping("/monthly-counts")
    public ResponseEntity<List<MonthlyCount>> getMonthlyCounts(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        // Default to last 12 months if no dates provided
        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            endDate = now.toString();
            startDate = now.minusMonths(12).toString();
        }

        List<MonthlyCount> results = opportunityRepository.findMonthlyOpportunityCounts(
                startDate, endDate);
        return ResponseEntity.ok(results);
    }

    // 3. Views Distribution
    @GetMapping("/views-distribution")
    public ResponseEntity<ViewsDistribution> getViewsDistribution() {
        ViewsDistribution results = opportunityRepository.findViewsDistribution();
        return ResponseEntity.ok(results);
    }

    // 4. Summary Statistics
    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummary> getSummaryStats() {
        AnalyticsSummary summary = opportunityRepository.findAnalyticsSummary();
        return ResponseEntity.ok(summary);
    }
}

// DTO Classes

