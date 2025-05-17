package org.backend.volunteeringbackend.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="VolunteerAssigneeOpportunity")
public class Application {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;


    private UUID userId;

    private UUID opportunityId;

    public Application() {

    }

    public Application(UUID userId, UUID opportunityId) {
        this.userId = userId;
        this.opportunityId = opportunityId;
    }

}
