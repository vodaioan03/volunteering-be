package org.backend.volunteeringbackend.Repository;

import jakarta.transaction.Transactional;
import org.backend.volunteeringbackend.DTO.AnalyticsSummary;
import org.backend.volunteeringbackend.DTO.MonthlyCount;
import org.backend.volunteeringbackend.DTO.OrganizerCount;
import org.backend.volunteeringbackend.DTO.ViewsDistribution;
import org.backend.volunteeringbackend.Models.Opportunity;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOpportunityRepository extends JpaRepository<Opportunity, UUID> {
    @Query("SELECT o FROM Opportunity o WHERE o.title = :title")
    Optional<Opportunity> findByTitle(@Param("title") String title);

    @Override
    @Transactional
    default <S extends Opportunity> S save(S entity) {
        System.out.println("Saving entity to database: " + entity);
        S saved = saveAndFlush(entity);
        System.out.println("Saved entity with ID: " + saved.getId());
        return saved;
    }
    // Custom query for featured opportunities (top 2 most viewed)
    @Query("SELECT o FROM Opportunity o ORDER BY o.views DESC LIMIT 2")
    List<Opportunity> findTop2ByOrderByViewsDesc();

    // Ascending sort by endDate
    List<Opportunity> findAllByOrderByEndDateAsc();

    // Descending sort by endDate
    List<Opportunity> findAllByOrderByEndDateDesc();

    // Optional: Filter opportunities ending soon
    List<Opportunity> findByEndDateBetween(String startDate, String endDate);

    // Optional: Count opportunities by organizer
    @Query("SELECT COUNT(o) FROM Opportunity o WHERE o.organizer = :organizer")
    long countByOrganizer(String organizer);

    // Optional: Find opportunities with minimum views
    List<Opportunity> findByViewsGreaterThanEqual(String minViews);


    // 1. Top Organizers - already well optimized with LIMIT
    @Query(value = """
    SELECT organizer as organizer, COUNT(*) as count 
    FROM opportunities 
    GROUP BY organizer 
    ORDER BY count DESC 
    LIMIT :limit
    """, nativeQuery = true)
    List<OrganizerCount> findTopOrganizers(@Param("limit") int limit);

    // 2. Monthly Counts - add date range parameters
    @Query(value = """
    SELECT
        TO_CHAR(TO_DATE(end_date, 'YYYY-MM-DD'), 'MM/YYYY') as monthYear,
        COUNT(*) as count
    FROM opportunities
    WHERE TO_DATE(end_date, 'YYYY-MM-DD') BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')
    GROUP BY TO_CHAR(TO_DATE(end_date, 'YYYY-MM-DD'), 'MM/YYYY')
    ORDER BY MIN(TO_DATE(end_date, 'YYYY-MM-DD'))
    """, nativeQuery = true)
    List<MonthlyCount> findMonthlyOpportunityCounts(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    // 3. Views Distribution - consider materializing this
    @Query(value = """
    SELECT 
        SUM(CASE WHEN views_numeric > 5000 THEN 1 ELSE 0 END) as highViews,
        SUM(CASE WHEN views_numeric BETWEEN 1000 AND 5000 THEN 1 ELSE 0 END) as mediumViews,
        SUM(CASE WHEN views_numeric < 1000 THEN 1 ELSE 0 END) as lowViews
    FROM (
        SELECT CAST(REPLACE(views, 'k', '') AS numeric) * 1000 as views_numeric
        FROM opportunities
    ) t
    """, nativeQuery = true)
    ViewsDistribution findViewsDistribution();
    // 4. Summary Stats
    @Query(value = """
    WITH stats AS (
        SELECT 
            COUNT(*) as total,
            AVG(CAST(REPLACE(views, 'k', '') AS numeric) * 1000) as avgViews,
            SUM(CASE WHEN TO_DATE(end_date, 'YYYY-MM-DD') BETWEEN CURRENT_DATE AND CURRENT_DATE + 7 THEN 1 ELSE 0 END) as endingSoon
        FROM opportunities
    ),
    top_org AS (
        SELECT organizer 
        FROM opportunities 
        GROUP BY organizer 
        ORDER BY COUNT(*) DESC 
        LIMIT 1
    )
    SELECT 
        s.total as total,
        s.avgViews as avgViews,
        s.endingSoon as endingSoon,
        t.organizer as topOrganizer
    FROM stats s, top_org t
    """, nativeQuery = true)
    public AnalyticsSummary findAnalyticsSummary();
}
