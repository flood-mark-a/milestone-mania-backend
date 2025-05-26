package com.milestonemania.service.api.dto;

import com.milestonemania.service.api.dto.AttemptStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for player game attempt sessions.
 * <p>
 * Represents an individual player's attempt at a specific game,
 * including progress tracking and milestone information.
 * 
 * @author Milestone Mania Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameAttemptDto {

    /**
     * Unique identifier for this specific attempt.
     */
    private Long attemptId;

    /**
     * Human-readable slug of the associated game.
     */
    private String gameSlug;

    /**
     * Optional name of the player making this attempt.
     */
    private String playerName;

    /**
     * Current status of the attempt (IN_PROGRESS or COMPLETED).
     */
    private AttemptStatus status;

    /**
     * Number of submission attempts made by the player.
     * Increments with each incorrect submission.
     */
    private Integer attemptCount;

    /**
     * Timestamp when the attempt was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the attempt was completed successfully (null if IN_PROGRESS).
     */
    private LocalDateTime completedAt;

    /**
     * List of exactly 5 milestones for this game attempt.
     * Milestones are presented without dates to prevent cheating.
     */
    private List<MilestoneDto> milestones;
}