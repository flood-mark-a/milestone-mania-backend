package com.milestonemania.repository;

import com.milestonemania.model.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Milestone entities.
 * Provides data access operations for milestone management.
 */
@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    /**
     * Finds a random selection of milestones.
     * Uses database-specific random function for better performance.
     * 
     * @param count the number of random milestones to retrieve
     * @return list of randomly selected milestones
     */
    @Query(value = "SELECT * FROM milestones ORDER BY RANDOM() LIMIT :count",
           nativeQuery = true)
    List<Milestone> findRandomMilestones(@Param("count") int count);

    /**
     * Alternative implementation for databases that don't support RANDOM().
     * PostgreSQL version - uncomment if using PostgreSQL
     */
    // @Query(value = "SELECT * FROM milestones ORDER BY RANDOM() LIMIT :count",
    //        nativeQuery = true)
    
    /**
     * MySQL version - uncomment if using MySQL
     */
    // @Query(value = "SELECT * FROM milestones ORDER BY RAND() LIMIT :count",
    //        nativeQuery = true)

    /**
     * Counts total number of milestones available.
     * Used to verify sufficient milestones exist for game creation.
     * 
     * @return total count of milestones
     */
    @Override
    long count();

    /**
     * Finds milestones containing specific text in title or description.
     * Useful for search functionality.
     * 
     * @param searchTerm the term to search for
     * @return list of matching milestones
     */
    @Query("SELECT m FROM Milestone m WHERE " +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Milestone> findByTitleOrDescriptionContainingIgnoreCase(
        @Param("searchTerm") String searchTerm);
}

