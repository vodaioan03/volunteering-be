package org.backend.volunteeringbackend.DTO;

import lombok.Data;

@Data
public class OpportunityCreateDTO {
    private String title;
    private String organizer;
    private String shortDescription;
    private String description;
    private String image;
    private String endDate;
    private String views;

    public OpportunityCreateDTO(String title, String organizer, String shortDescription, String description, String image, String endDate, String views) {
        this.title = title;
        this.organizer = organizer;
        this.shortDescription = shortDescription;
        this.description = description;
        this.image = image;
        this.endDate = endDate;
        this.views = views;
    }

    public OpportunityCreateDTO() {}

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
}