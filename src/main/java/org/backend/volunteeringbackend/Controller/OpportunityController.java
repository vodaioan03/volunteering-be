package org.backend.volunteeringbackend.Controller;

import org.apache.coyote.BadRequestException;
import org.backend.volunteeringbackend.DTO.OpportunityCreateDTO;
import org.backend.volunteeringbackend.DTO.OpportunityUpdateDTO;
import org.backend.volunteeringbackend.Models.Opportunity;
import org.backend.volunteeringbackend.Services.ApplicationService;
import org.backend.volunteeringbackend.Services.OpportunityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {
    private final OpportunityService opportunityService;
    private final OpportunityWebSocketController webSocketController;
    private final ApplicationService applicationService;

    public OpportunityController(OpportunityService opportunityService,
                                 OpportunityWebSocketController webSocketController,
                                 ApplicationService applicationService) {
        this.opportunityService = opportunityService;
        this.webSocketController = webSocketController;
        this.applicationService = applicationService;
    }


    // Paginated endpoint
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getPaginatedOpportunities(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Opportunity> opportunityPage = opportunityService.getPaginatedOpportunities(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", opportunityPage.getContent());
        response.put("total", opportunityPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    // Ascending sorted paginated
    @GetMapping("/get-ascending-order")
    public ResponseEntity<Map<String, Object>> getAscendingSorted(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by("endDate").ascending()
        );

        Page<Opportunity> opportunityPage = opportunityService.getPaginatedOpportunities(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", opportunityPage.getContent());
        response.put("total", opportunityPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-descending-order")
    public ResponseEntity<Map<String, Object>> getDescendingSorted(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by("endDate").descending()
        );

        Page<Opportunity> opportunityPage = opportunityService.getPaginatedOpportunities(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", opportunityPage.getContent());
        response.put("total", opportunityPage.getTotalElements());

        return ResponseEntity.ok(response);
    }
    // Featured opportunities endpoint
    @GetMapping("/featured")
    public ResponseEntity<List<Opportunity>> getFeaturedOpportunities() {
        return ResponseEntity.ok(opportunityService.getFeaturedOpportunities());
    }

    @GetMapping
    public ResponseEntity<List<Opportunity>> getAllOpportunities() {
        return ResponseEntity.ok(opportunityService.getAllOpportunities());
    }

    @GetMapping("/getOpportunityById/{id}")
    public ResponseEntity<Opportunity> getOpportunityById(@PathVariable UUID id) {
        return ResponseEntity.ok(opportunityService.getById(id));
    }


    @PostMapping
    public ResponseEntity<Opportunity> createOpportunity(@RequestBody OpportunityCreateDTO createDTO) {
        try {
            Opportunity created = null;
            System.err.println(createDTO.getEndDate());
            created = opportunityService.createOpportunity(createDTO);

            // Broadcast update after creation
            webSocketController.broadcastOpportunitiesUpdate();
            System.err.println("In try ce plm");
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(created.getId().toString())
                    .toUri();
            return ResponseEntity.created(location).body(created);
        } catch (BadRequestException e) {
            ResponseEntity.badRequest().body(e.getMessage());
            System.err.println("in catch " + e.getMessage());
            return ResponseEntity.ok().body(null);
        }

    }

    @GetMapping("/initialOpportunity")
    public ResponseEntity<Opportunity> initialOpportunity() {
        try {
            this.opportunityService.addSampleData();
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/updateOpportunity/{id}")
    public ResponseEntity<Opportunity> updateOpportunity(
            @PathVariable UUID id,
            @RequestBody OpportunityUpdateDTO updateDTO) {
        Opportunity updated = opportunityService.updateOpportunity(id, updateDTO);

        // Broadcast update after modification
        webSocketController.broadcastOpportunitiesUpdate();

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleteOpportunity/{id}")
    public ResponseEntity<String> deleteOpportunity(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            opportunityService.deleteOpportunity(uuid);

            // Return explicit empty response with content type
            return ResponseEntity.noContent()
                    .header("Content-Type", "application/json")
                    .build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Invalid ID format\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Deletion failed\"}");
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        // Add any additional health checks here if needed
        // For example: database connection check, external service status, etc.
        return new ResponseEntity<>("Service is healthy", HttpStatus.OK);
    }

    // Alternative HEAD method version
    @RequestMapping(value = "/health", method = RequestMethod.HEAD)
    public ResponseEntity<Void> healthCheckHead() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}