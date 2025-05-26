package com.milestonemania.repository;

import com.milestonemania.model.entity.Game;
import com.milestonemania.model.entity.GameAttempt;
import com.milestonemania.model.enums.AttemptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for GameAttempt entities.
 * Manages player attempts and provides leaderboard functionality.
 */
@Repository
public interface GameAttemptRepository extends JpaRepository<GameAttempt, Long> {

    /**
     * Finds a game attempt by ID and status.
     * Used to validate that an attempt is still active before allowing updates.
     * 
     * @param id the attempt ID
     * @param status the required status
     * @return Optional containing the attempt if found with matching status
     */
    Optional<GameAttempt> findByIdAndStatus(Long id, AttemptStatus status);

    /**
     * Finds all attempts for a game ordered by attempt count (golf scoring).
     * Lower attempt counts rank higher on the leaderboard.
     * 
     * @param game the game to find attempts for
     * @return list of attempts ordered by attempt count (best scores first)
     */
    @Query("SELECT ga FROM GameAttempt ga WHERE ga.game = :game " +
           "AND ga.status = 'COMPLETED' " +
           "ORDER BY ga.attemptCount ASC, ga.completedAt ASC")
    List<GameAttempt> findByGameOrderByAttemptCountAsc(@Param("game") Game game);

    /**
     * Finds attempts for a game with pagination support.
     * Useful for large leaderboards.
     * 
     * @param game the game
     * @param pageable pagination parameters
     * @return page of completed attempts ordered by score
     */
    @Query("SELECT ga FROM GameAttempt ga WHERE ga.game = :game " +
           "AND ga.status = 'COMPLETED' " +
           "ORDER BY ga.attemptCount ASC, ga.completedAt ASC")
    Page<GameAttempt> findCompletedAttemptsByGame(@Param("game") Game game, 
                                                 Pageable pageable);

    /**
     * Counts total attempts for a game.
     * Includes both completed and in-progress attempts.
     * 
     * @param game the game to count attempts for
     * @return total number of attempts
     */
    long countByGame(Game game);

    /**
     * Counts completed attempts for a game.
     * Used for completion statistics.
     * 
     * @param game the game
     * @return number of completed attempts
     */
    long countByGameAndStatus(Game game, AttemptStatus status);

    /**
     * Finds attempts by player name across all games.
     * Used for player statistics and history.
     * 
     * @param playerName the player name
     * @return list of attempts by the player
     */
    List<GameAttempt> findByPlayerNameOrderByCreatedAtDesc(String playerName);

    /**
     * Finds the best (lowest) attempt count for a completed game.
     * Used for game statistics and difficulty assessment.
     * 
     * @param game the game
     * @return Optional containing the minimum attempt count, or empty if no completions
     */
    @Query("SELECT MIN(ga.attemptCount) FROM GameAttempt ga " +
           "WHERE ga.game = :game AND ga.status = 'COMPLETED'")
    Optional<Integer> findMinAttemptCountByGame(@Param("game") Game game);

    /**
     * Finds attempts created within a date range.
     * Useful for analytics and activity tracking.
     * 
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of attempts created in the range
     */
    List<GameAttempt> findByCreatedAtBetweenOrderByCreatedAtDesc(
        LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds in-progress attempts older than a specified date.
     * Used for cleanup of abandoned attempts.
     * 
     * @param cutoffDate attempts older than this date
     * @return list of stale in-progress attempts
     */
    @Query("SELECT ga FROM GameAttempt ga WHERE ga.status = 'IN_PROGRESS' " +
           "AND ga.createdAt < :cutoffDate")
    List<GameAttempt> findStaleInProgressAttempts(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Finds the top N attempts for a game (leaderboard).
     * 
     * @param game the game
     * @param limit maximum number of attempts to return
     * @return list of top attempts
     */
    @Query("SELECT ga FROM GameAttempt ga WHERE ga.game = :game " +
           "AND ga.status = 'COMPLETED' " +
           "ORDER BY ga.attemptCount ASC, ga.completedAt ASC " +
           "LIMIT :limit")
    List<GameAttempt> findTopAttemptsByGame(@Param("game") Game game, 
                                           @Param("limit") int limit);
}

