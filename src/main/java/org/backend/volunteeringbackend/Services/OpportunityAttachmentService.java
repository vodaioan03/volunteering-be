package org.backend.volunteeringbackend.Services;

import org.backend.volunteeringbackend.Exceptions.ResourceNotFoundException;
import org.backend.volunteeringbackend.DTO.AttachmentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OpportunityAttachmentService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<AttachmentDTO> getAttachments(String opportunityId) {
        // Create the path to the opportunity's attachment directory
        Path opportunityDir = Paths.get(uploadDir, opportunityId);

        // Check if directory exists
        if (!Files.exists(opportunityDir)) {
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.list(opportunityDir)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            // Get basic file attributes
                            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

                            // Try to determine content type
                            String contentType = Files.probeContentType(path);
                            if (contentType == null) {
                                contentType = "application/octet-stream";
                            }

                            // Create DTO for each file
                            return new AttachmentDTO(
                                    path.getFileName().toString(), // Using filename as ID (you might want to use a different ID system)
                                    getOriginalFileName(path),    // Implement this method to retrieve original filename
                                    contentType,
                                    attrs.size(),
                                    opportunityId
                            );
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to read file attributes: " + path, e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list attachments for opportunity: " + opportunityId, e);
        }
    }

    // Helper method to get original filename (you might store this in a database or metadata file)
    private String getOriginalFileName(Path filePath) {
        // In a real implementation, you might:
        // 1. Store original filenames in a database
        // 2. Use a metadata file in the same directory
        // 3. Encode the original filename in the stored filename

        // For this example, we'll just return the filename
        return filePath.getFileName().toString();
    }

    public AttachmentDTO saveAttachment(String opportunityId, MultipartFile file) {
        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file");
        }

        // Generate file ID and path
        String fileId = UUID.randomUUID().toString();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path uploadPath = Paths.get(uploadDir, opportunityId);

        try {
            // Create directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file
            Path filePath = uploadPath.resolve(fileId);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Create and return attachment metadata
            return new AttachmentDTO(
                    fileId,
                    fileName,
                    file.getContentType(),
                    file.getSize(),
                    opportunityId
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public AttachmentDTO getAttachment(String opportunityId, String fileId) {
        // Validate parameters
        if (opportunityId == null || opportunityId.isEmpty() || fileId == null || fileId.isEmpty()) {
            throw new IllegalArgumentException("Opportunity ID and File ID must not be empty");
        }

        // Create the path to the specific attachment file
        Path filePath = Paths.get(uploadDir, opportunityId, fileId);

        // Check if file exists
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("Attachment not found with ID: " + fileId);
        }

        try {
            // Get file attributes
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);

            // Determine content type
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // In a real implementation, you would need a way to get the original filename
            // This could be stored in a separate metadata file or database
            String originalFileName = getOriginalFileName(fileId); // Implement this method

            return new AttachmentDTO(
                    fileId,
                    originalFileName,
                    contentType,
                    attrs.size(),
                    opportunityId
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read attachment metadata: " + fileId, e);
        }
    }

    private String getOriginalFileName(String fileId) {
        // Implement this based on how you store original filenames
        // This could be:
        // 1. Reading from a metadata file
        // 2. Looking up in a database
        // 3. Decoding from the fileId if you encoded it

        // For now, just return the fileId as a placeholder
        return fileId;
    }

    public Resource getAttachmentAsResource(AttachmentDTO attachment) {
        try {
            Path filePath = Paths.get(uploadDir, attachment.getOpportunityId(), attachment.getId());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + attachment.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + attachment.getFileName(), e);
        }
    }
}