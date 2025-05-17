package org.backend.volunteeringbackend.DTO;

import lombok.Data;

@Data
public class AttachmentDTO {
    private String id;
    private String fileName;
    private String contentType;
    private long size;
    private String opportunityId;

    public AttachmentDTO(String id, String fileName, String contentType, long size, String opportunityId) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.opportunityId = opportunityId;
    }

    // Constructors, getters, and setters
}