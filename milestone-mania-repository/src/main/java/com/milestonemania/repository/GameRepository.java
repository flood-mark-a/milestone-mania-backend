package com.milestonemania.repository;

import com.milestonemania.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Game entities.
 * Provides data access operations for game management.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Finds a game by its unique slug.
     * The slug is used for sharing and accessing games via URL.
     * 
     * @param slug the unique game slug
     * @return Optional containing the game if found
     */
    Optional<Game> findBySlug(String slug);

    /**
     * Checks if a game with the given slug already exists.
     * Useful for ensuring slug uniqueness during game creation.
     * 
     * @param slug the slug to check
     * @return true if a game with this slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Finds games created after a specific date.
     * Useful for analytics and recent games display.
     * 
     * @param date the cutoff date
     * @return list of games created after the specified date
     */
    List<Game> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);

    /**
     * Finds the most recently created games.
     * Limited to prevent performance issues.
     * 
     * @param limit maximum number of games to return
     * @return list of recent games
     */
    @Query("SELECT g FROM Game g ORDER BY g.createdAt DESC LIMIT :limit")
    List<Game> findRecentGames(@Param("limit") int limit);

    /**
     * Counts games created within a date range.
     * Useful for analytics and reporting.
     * 
     * @param startDate start of the date range
     * @param endDate end of the date range
     * @return count of games created in the range
     */
    @Query("SELECT COUNT(g) FROM Game g WHERE g.createdAt BETWEEN :startDate AND :endDate")
    long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);
}

