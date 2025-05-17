package org.backend.volunteeringbackend.DTO;

public class MonthlyCount {
    private String monthYear;
    private Long count;

    // Constructor must match SQL query result columns (monthYear, count)
    public MonthlyCount(String monthYear, Long count) {
        this.monthYear = monthYear;
        this.count = count;
    }

    // Getters
    public String getMonthYear() { return monthYear; }
    public Long getCount() { return count; }
}