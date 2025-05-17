package org.backend.volunteeringbackend;

import org.backend.volunteeringbackend.DTO.OpportunityCreateDTO;
import org.backend.volunteeringbackend.DTO.OpportunityUpdateDTO;
import org.backend.volunteeringbackend.Models.Opportunity;
import org.backend.volunteeringbackend.Repository.OpportunityRepository;
import org.backend.volunteeringbackend.Services.OpportunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // ✅ Ensures Mockito works properly
class OpportunityTests {

    @Mock
    private OpportunityRepository opportunityRepository;

    @InjectMocks
    private OpportunityService opportunityService;

    private Opportunity sampleOpportunity;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleOpportunity = new Opportunity();
        sampleOpportunity.setId(sampleId);
        sampleOpportunity.setTitle("Sample Title");
        sampleOpportunity.setOrganizer("Sample Organizer");
        sampleOpportunity.setShortDescription("Short Desc");
        sampleOpportunity.setDescription("Full Desc");
        sampleOpportunity.setImage("image.jpg");
        sampleOpportunity.setEndDate("2025-12-31");
        sampleOpportunity.setViews("0");
    }

    @Test
    void testGetAllOpportunities() {
        List<Opportunity> opportunities = List.of(sampleOpportunity);
        when(opportunityRepository.findAll()).thenReturn(opportunities);

        List<Opportunity> result = opportunityService.getAllOpportunities();
        assertEquals(1, result.size());
        assertEquals("Sample Title", result.get(0).getTitle());
    }

    @Test
    void testGetById() {
        when(opportunityRepository.findById(sampleId)).thenReturn(Optional.of(sampleOpportunity));

        Opportunity result = opportunityService.getById(sampleId);
        assertNotNull(result);
        assertEquals("Sample Title", result.getTitle());
    }

    @Test
    void testGetByTitle() {
        when(opportunityRepository.findByTitle("Sample Title")).thenReturn(Optional.of(sampleOpportunity));

        Opportunity result = opportunityService.getByTitle("Sample Title");
        assertNotNull(result);
        assertEquals("Sample Title", result.getTitle());
    }

    @Test
    void testCreateOpportunity() throws Exception {
        OpportunityCreateDTO createDTO = new OpportunityCreateDTO("Sample Title", "Sample Organizer", "Short Desc", "Full Desc", "image.jpg", "2025-12-31","0");
        when(opportunityRepository.save(any(Opportunity.class))).thenReturn(sampleOpportunity);

        Opportunity result = opportunityService.createOpportunity(createDTO);
        assertNotNull(result);
        assertEquals("Sample Title", result.getTitle());
    }

    @Test
    void testUpdateOpportunity() {
        OpportunityUpdateDTO updateDTO = new OpportunityUpdateDTO("Updated Title", "Updated Organizer", "Updated Desc", "Updated Short Desc", "updated.jpg", "2026-01-01");
        when(opportunityRepository.findById(sampleId)).thenReturn(Optional.of(sampleOpportunity));
        when(opportunityRepository.save(any(Opportunity.class))).thenReturn(sampleOpportunity);

        Opportunity result = opportunityService.updateOpportunity(sampleId, updateDTO);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void testDeleteOpportunity() {
        when(opportunityRepository.findById(sampleId)).thenReturn(Optional.of(sampleOpportunity));
        doNothing().when(opportunityRepository).delete(sampleOpportunity);

        assertDoesNotThrow(() -> opportunityService.deleteOpportunity(sampleId));
        verify(opportunityRepository, times(1)).delete(sampleOpportunity);
    }

    @Test
    void testGetAscendingSorted() {
        List<Opportunity> opportunities = new ArrayList<>();
        Opportunity opp1 = new Opportunity(); opp1.setTitle("A Title");
        Opportunity opp2 = new Opportunity(); opp2.setTitle("B Title");
        opportunities.add(opp2);
        opportunities.add(opp1);

        when(opportunityRepository.findAll()).thenReturn(opportunities);
        List<Opportunity> result = opportunityService.getAscendingSorted();

        assertEquals("A Title", result.get(0).getTitle());
        assertEquals("B Title", result.get(1).getTitle());
    }

    @Test
    void testGetDescendingSorted() { // ✅ Fixed typo from "getDescenndingSorted()"
        List<Opportunity> opportunities = new ArrayList<>();
        Opportunity opp1 = new Opportunity(); opp1.setTitle("A Title");
        Opportunity opp2 = new Opportunity(); opp2.setTitle("B Title");
        opportunities.add(opp1);
        opportunities.add(opp2);

        when(opportunityRepository.findAll()).thenReturn(opportunities);
        List<Opportunity> result = opportunityService.getDescenndingSorted(); // ✅ Fixed method name

        assertEquals("B Title", result.get(0).getTitle());
        assertEquals("A Title", result.get(1).getTitle());
    }
}
