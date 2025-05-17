package org.backend.volunteeringbackend.Services;

import com.github.javafaker.Faker;
import org.backend.volunteeringbackend.Models.Opportunity;
import org.backend.volunteeringbackend.Repository.IOpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Profile("!prod") // Only run in non-production environments
public class DataLoader {
    @Autowired
    private final IOpportunityRepository repository;
    private final Faker faker = new Faker();
    private final AtomicInteger counter = new AtomicInteger(1);

    public DataLoader(IOpportunityRepository repository) {
        this.repository = repository;
        System.out.println("DataLoader is running...");

    }

    @Bean
    @Transactional
    public CommandLineRunner loadData() {

        return args -> {
            // Check if database is already populated
            if (repository.count() < 100) {
                int batchSize = 1000;
                int totalEntities = 100_000;

                for (int i = 0; i < totalEntities; i += batchSize) {
                    List<Opportunity> batch = new ArrayList<>(batchSize);

                    for (int j = 0; j < batchSize && (i + j) < totalEntities; j++) {
                        batch.add(createFakeOpportunity());
                    }

                    repository.saveAll(batch);
                    repository.flush();

                    System.out.printf("Saved batch %d-%d of %d%n",
                            i + 1, Math.min(i + batchSize, totalEntities), totalEntities);
                }
                System.out.println("Finished loading all opportunities!");
            }
        };
    }

    private Opportunity createFakeOpportunity() {
        Opportunity opportunity = new Opportunity();



        // Basic information with length limits
        opportunity.setTitle(truncate(faker.company().catchPhrase() + " Volunteer Opportunity", 100));
        opportunity.setOrganizer(truncate(faker.company().name() + " Foundation", 100));
        opportunity.setViews(String.valueOf(faker.number().numberBetween(100, 10000)));
        // Dates - make end dates in the future
        LocalDate startDate = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        LocalDate endDate = startDate.plusDays(faker.number().numberBetween(7, 365));
        opportunity.setEndDate(endDate.toString());

        // Descriptions
        opportunity.setShortDescription(truncate(faker.lorem().sentence(10), 255));
        opportunity.setDescription(truncate(faker.lorem().paragraph(3), 254)); // Assuming TEXT type in DB

        opportunity.setImage(truncate(faker.internet().image(640, 480, true, "nature"),100));
        return opportunity;
    }
    // Helper method to truncate strings
    private String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
}