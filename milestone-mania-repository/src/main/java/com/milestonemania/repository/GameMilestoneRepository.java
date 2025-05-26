package com.milestonemania.repository;

import com.milestonemania.model.entity.Game;
import com.milestonemania.model.entity.GameMilestone;
import com.milestonemania.model.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for GameMilestone entities.
 * Manages the relationship between games and their associated milestones.
 */
@Repository
public interface GameMilestoneRepository extends JpaRepository<GameMilestone, Long> {

    /**
     * Finds all milestones for a game ordered by their correct chronological order.
     * This provides the solution order for validation and display.
     * 
     * @param game the game to find milestones for
     * @return list of game milestones in correct chronological order
     */
    List<GameMilestone> findByGameOrderByCorrectOrder(Game game);

    /**
     * Finds all milestones for a game ordered by actual milestone date.
     * Alternative ordering method.
     * 
     * @param game the game to find milestones for
     * @return list of game milestones ordered by actual historical date
     */
    @Query("SELECT gm FROM GameMilestone gm WHERE gm.game = :game " +
           "ORDER BY gm.milestone.actualDate ASC")
    List<GameMilestone> findByGameOrderByMilestoneDate(@Param("game") Game game);

    /**
     * Deletes all game milestones associated with a specific game.
     * Used for cleanup when a game is deleted.
     * 
     * @param game the game whose milestones should be deleted
     */
    @Modifying
    @Transactional
    void deleteByGame(Game game);

    /**
     * Finds a specific game milestone by game and milestone.
     * Useful for validation and duplicate prevention.
     * 
     * @param game the game
     * @param milestone the milestone
     * @return Optional containing the game milestone if found
     */
    Optional<GameMilestone> findByGameAndMilestone(Game game, Milestone milestone);

    /**
     * Counts the number of milestones in a game.
     * Used for validation and game statistics.
     * 
     * @param game the game to count milestones for
     * @return count of milestones in the game
     */
    long countByGame(Game game);

    /**
     * Finds game milestones within a specific order range.
     * Useful for partial game loading or validation.
     * 
     * @param game the game
     * @param minOrder minimum order value (inclusive)
     * @param maxOrder maximum order value (inclusive)
     * @return list of game milestones within the order range
     */
    @Query("SELECT gm FROM GameMilestone gm WHERE gm.game = :game " +
           "AND gm.correctOrder BETWEEN :minOrder AND :maxOrder " +
           "ORDER BY gm.correctOrder")
    List<GameMilestone> findByGameAndCorrectOrderBetween(
        @Param("game") Game game, 
        @Param("minOrder") Integer minOrder, 
        @Param("maxOrder") Integer maxOrder);
}

