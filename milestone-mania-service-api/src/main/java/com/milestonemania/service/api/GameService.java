package com.milestonemania.service.api;

import com.milestonemania.service.api.dto.GameAttemptDto;
import com.milestonemania.service.api.dto.GameDto;
import com.milestonemania.service.api.dto.SubmitAttemptRequest;
import com.milestonemania.service.api.dto.SubmitAttemptResponse;
import com.milestonemania.service.api.exception.AttemptNotFoundException;
import com.milestonemania.service.api.exception.GameNotFoundException;
import com.milestonemania.service.api.exception.InvalidAttemptStateException;
import com.milestonemania.service.api.exception.InsufficientMilestonesException;

/**
 * Core service interface for managing timeline ordering games.
 * Handles game creation, attempt management, and submission processing.
 * 
 * @author Milestone Mania Team
 * @version 1.0
 */
public interface GameService {

    /**
     * Creates a new game with 5 randomly selected milestones.
     * <p>
     * This method performs the following operations:
     * <ul>
     *   <li>Randomly selects 5 milestones from available pool</li>
     *   <li>Creates new Game entity with human-readable slug (e.g., "donkey-football-glove")</li>
     *   <li>Creates GameMilestone entries with correct chronological ordering</li>
     *   <li>Creates new GameAttempt for the player with status IN_PROGRESS</li>
     * </ul>
     * 
     * @param playerName optional player name for the attempt
     * @return GameAttemptDto containing milestones (without dates), attemptId, and game slug
     * @throws InsufficientMilestonesException if fewer than 5 milestones are available in the system
     */
    GameAttemptDto createNewGame(String playerName);

    /**
     * Finds existing game by slug and creates new attempt for player.
     * <p>
     * This method performs the following operations:
     * <ul>
     *   <li>Looks up Game by slug, throws GameNotFoundException if not found</li>
     *   <li>Creates new GameAttempt for this player using existing game template</li>
     *   <li>Returns same milestones as original game (via GameMilestone relationships)</li>
     * </ul>
     * 
     * @param slug the game's unique slug identifier
     * @param playerName optional player name for the new attempt
     * @return GameAttemptDto with same milestones as template game
     * @throws GameNotFoundException if slug doesn't exist
     */
    GameAttemptDto startGameFromSlug(String slug, String playerName);

    /**
     * Processes player's milestone ordering submission.
     * <p>
     * This method handles submission logic:
     * <ul>
     *   <li>Validates attempt exists and is IN_PROGRESS</li>
     *   <li>Compares submitted order against GameMilestone.correctOrder</li>
     *   <li>If perfect: marks attempt COMPLETED, sets completedAt timestamp</li>
     *   <li>If incorrect: increments attemptCount, keeps status IN_PROGRESS</li>
     *   <li>Returns feedback on correctness and sharing info if completed</li>
     * </ul>
     * 
     * @param request contains attemptId and list of 5 milestone IDs in player's order
     * @return SubmitAttemptResponse with success/failure status and next steps
     * @throws AttemptNotFoundException if attemptId is invalid
     * @throws InvalidAttemptStateException if attempt is already completed
     */
    SubmitAttemptResponse submitAttempt(SubmitAttemptRequest request);

    /**
     * Retrieves game template information without creating new attempt.
     * <p>
     * Used for sharing/preview functionality. Returns game metadata 
     * and milestone list for display purposes.
     * 
     * @param slug the game's unique slug identifier
     * @return GameDto with template information including milestones
     * @throws GameNotFoundException if slug doesn't exist
     */
    GameDto getGameBySlug(String slug);
}