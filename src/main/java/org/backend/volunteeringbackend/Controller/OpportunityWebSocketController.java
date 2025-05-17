package org.backend.volunteeringbackend.Controller;

import org.backend.volunteeringbackend.Services.OpportunityService;
import org.backend.volunteeringbackend.Models.Opportunity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OpportunityWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final OpportunityService opportunityService;

    public OpportunityWebSocketController(
            SimpMessagingTemplate messagingTemplate,
            OpportunityService opportunityService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.opportunityService = opportunityService;
    }

    public void broadcastOpportunitiesUpdate() {
        List<Opportunity> opportunities = opportunityService.getAllOpportunities();
        messagingTemplate.convertAndSend("/topic/opportunities", opportunities);
    }
}