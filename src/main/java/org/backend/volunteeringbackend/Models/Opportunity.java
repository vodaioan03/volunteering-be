// Opportunity.java
package org.backend.volunteeringbackend.Models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "opportunities", schema = "public")
public class Opportunity {
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String title;
    private String organizer;

    @Column(name = "short_description")  // Maps to Supabase's snake_case
    private String shortDescription;

    private String description;
    private String image;

    @Column(columnDefinition = "text default '0'")
    private String views;

    @Column(name = "end_date")           // Maps to Supabase's snake_case
    private String endDate;
    public Opportunity(String title, String organizer, String shortDescription,
                       String description, String image, String views, String endDate) {
        this.title = title;
        this.organizer = organizer;
        this.shortDescription = shortDescription;
        this.description = description;
        this.image = image;
        this.views = views;
        this.endDate = endDate;
    }
    public Opportunity() {}
}