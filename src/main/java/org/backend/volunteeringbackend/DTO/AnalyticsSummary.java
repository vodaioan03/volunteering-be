package org.backend.volunteeringbackend.DTO;

import java.math.BigDecimal;

public class AnalyticsSummary {
    private final Long total;
    private final BigDecimal avgViews;  // Changed from Double to BigDecimal
    private final Long endingSoon;
    private final String topOrganizer;

    public AnalyticsSummary(Long total, BigDecimal avgViews, Long endingSoon, String topOrganizer) {
        this.total = total;
        this.avgViews = avgViews;
        this.endingSoon = endingSoon;
        this.topOrganizer = topOrganizer;
    }

    // Getters
    public Long getTotal() { return total; }
    public BigDecimal getAvgViews() { return avgViews; }
    public Long getEndingSoon() { return endingSoon; }
    public String getTopOrganizer() { return topOrganizer; }

    // Optional: Add method to get avgViews as double if needed
    public double getAvgViewsAsDouble() {
        return avgViews != null ? avgViews.doubleValue() : 0.0;
    }
}