package org.backend.volunteeringbackend.DTO;

public class ViewsDistribution {
    private Long highViews;
    private Long mediumViews;
    private Long lowViews;

    // Constructor must match SQL query result columns (highViews, mediumViews, lowViews)
    public ViewsDistribution(Long highViews, Long mediumViews, Long lowViews) {
        this.highViews = highViews;
        this.mediumViews = mediumViews;
        this.lowViews = lowViews;
    }

    // Getters
    public Long getHighViews() { return highViews; }
    public Long getMediumViews() { return mediumViews; }
    public Long getLowViews() { return lowViews; }
}