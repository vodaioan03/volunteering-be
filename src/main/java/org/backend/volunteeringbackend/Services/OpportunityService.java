// OpportunityService.java
package org.backend.volunteeringbackend.Services;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.backend.volunteeringbackend.Exceptions.ResourceNotFoundException;
import org.backend.volunteeringbackend.DTO.OpportunityCreateDTO;
import org.backend.volunteeringbackend.DTO.OpportunityUpdateDTO;
import org.backend.volunteeringbackend.Repository.IOpportunityRepository;
import org.backend.volunteeringbackend.Models.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Transactional
public class OpportunityService {
    private final IOpportunityRepository opportunityRepository;

    public OpportunityService(IOpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    public List<Opportunity> getAllOpportunities() {
        return opportunityRepository.findAll();
    }

    public Opportunity getByTitle(String title) {
        return opportunityRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found"));
    }

    public Opportunity getById(UUID id) {
        return opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + id));
    }

    public Page<Opportunity> getPaginatedOpportunities(Pageable pageable) {
        return opportunityRepository.findAll(pageable);
    }

    public List<Opportunity> getFeaturedOpportunities() {
        // Implement your logic to get featured opportunities
        // For example, could be based on views, creation date, etc.
        return opportunityRepository.findTop2ByOrderByViewsDesc();
    }

    public List<Opportunity> getAscendingSorted() {
        return opportunityRepository.findAllByOrderByEndDateAsc();
    }

    public List<Opportunity> getDescendingSorted() {
        return opportunityRepository.findAllByOrderByEndDateDesc();
    }

    public void addSampleData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream(new File("src/main/java/org/backend/volunteeringbackend/opportunities.json"));

            if (inputStream != null) {
                Opportunity[] opportunitiesArray = mapper.readValue(inputStream, Opportunity[].class);
                List<Opportunity> sampleOpportunities = Arrays.asList(opportunitiesArray);
                opportunityRepository.saveAll(sampleOpportunities);
                System.out.println("Successfully loaded " + sampleOpportunities.size() + " opportunities from JSON");
            } else {
                System.err.println("Could not find opportunities.json in resources");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading opportunities from JSON");
        }
    }

    public Opportunity createOpportunity(OpportunityCreateDTO createDTO) throws BadRequestException {
        if(createDTO.getShortDescription().isEmpty() ||
           createDTO.getOrganizer().isEmpty() ||
           createDTO.getEndDate().isEmpty() ||
           createDTO.getImage().isEmpty() ||
           createDTO.getTitle().isEmpty() ||
           !createDTO.getEndDate().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new BadRequestException("All fields are missing");
        }
        
        Opportunity opportunity = new Opportunity();
        opportunity.setTitle(createDTO.getTitle());
        opportunity.setOrganizer(createDTO.getOrganizer());
        opportunity.setShortDescription(createDTO.getShortDescription());
        opportunity.setDescription(createDTO.getDescription());
        opportunity.setImage(createDTO.getImage());
        opportunity.setEndDate(createDTO.getEndDate());
        opportunity.setViews("0");
        return opportunityRepository.save(opportunity);
    }

    public Opportunity updateOpportunity(UUID id, OpportunityUpdateDTO updateDTO) {
        Opportunity existing = getById(id);

        if (updateDTO.getTitle() != null) {
            existing.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getOrganizer() != null) {
            existing.setOrganizer(updateDTO.getOrganizer());
        }
        if (updateDTO.getDescription() != null) {
            existing.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getImage() != null) {
            existing.setImage(updateDTO.getImage());
        }
        if (updateDTO.getEndDate() != null) {
            existing.setEndDate(updateDTO.getEndDate());
        }
        if (updateDTO.getShortDescription() != null) {
            existing.setShortDescription(updateDTO.getShortDescription());
        }

        return opportunityRepository.save(existing);
    }

    public void deleteOpportunity(UUID id) {
        Opportunity existing = getById(id);
        opportunityRepository.delete(existing);
    }
}