package org.backend.volunteeringbackend.Repository;

import org.backend.volunteeringbackend.Models.Opportunity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OpportunityRepository {
    private final ConcurrentHashMap<UUID, Opportunity> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public OpportunityRepository() {

    }


    public List<Opportunity> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Opportunity> findById(UUID id) {
        return store.values().stream()
                .filter(opp -> opp.getId().equals(id))
                .findFirst();
    }

    public Optional<Opportunity> findByTitle(String title) {
        return store.values().stream()
                .filter(opp -> opp.getTitle().equals(title))
                .findFirst();
    }

    public Opportunity save(Opportunity opportunity) {
        if (opportunity.getId() == null) {
            opportunity.setId(UUID.randomUUID());
            //opportunity.setCreatedAt(LocalDateTime.now());
        }
        //opportunity.setUpdatedAt(LocalDateTime.now());
        store.put(opportunity.getId(), opportunity);
        return opportunity;
    }


    public void delete(Opportunity opportunity) {
        store.remove(opportunity.getId());
    }
}