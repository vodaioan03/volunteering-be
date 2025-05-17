package org.backend.volunteeringbackend.Controller;


import org.backend.volunteeringbackend.Services.OpportunityAttachmentService;
import org.backend.volunteeringbackend.DTO.AttachmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities/{opportunityId}/attachments")
public class OpportunityAttachmentController {

    @Autowired
    private OpportunityAttachmentService attachmentService;

    @GetMapping
    public ResponseEntity<List<AttachmentDTO>> getAttachments(@PathVariable String opportunityId) {
        List<AttachmentDTO> attachments = attachmentService.getAttachments(opportunityId);
        return ResponseEntity.ok(attachments);
    }

    @PostMapping
    public ResponseEntity<AttachmentDTO> uploadAttachment(
            @PathVariable String opportunityId,
            @RequestParam("file") MultipartFile file) {

        AttachmentDTO attachment = attachmentService.saveAttachment(opportunityId, file);
        return ResponseEntity.ok(attachment);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable String opportunityId,
            @PathVariable String fileId) {

        AttachmentDTO attachment = attachmentService.getAttachment(opportunityId, fileId);
        Resource resource = attachmentService.getAttachmentAsResource(attachment);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(resource);
    }
}