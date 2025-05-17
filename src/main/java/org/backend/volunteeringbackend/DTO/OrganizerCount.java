package org.backend.volunteeringbackend.DTO;

public class OrganizerCount {
    private String organizer;
    private Long count;

    // Constructor must match SQL query result columns (organizer, count)
    public OrganizerCount(String organizer, Long count) {
        this.organizer = organizer;
        this.count = count;
    }

    // Getters (required for JSON serialization)
    public String getOrganizer() { return organizer; }
    public Long getCount() { return count; }
}
