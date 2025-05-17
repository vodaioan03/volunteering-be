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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

}