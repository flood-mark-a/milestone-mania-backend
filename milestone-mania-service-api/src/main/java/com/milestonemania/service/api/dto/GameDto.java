package com.milestonemania.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Game template information.
 * <p>
 * Contains game metadata and associated milestones for sharing
 * and preview functionality without creating new attempts.
 * 
 * @author Milestone Mania Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    /**
     * Unique identifier for the game.
     */
    private Long id;

    /**
     * Human-readable slug for the game (e.g., "donkey-football-glove").
     */
    private String slug;

    /**
     * Optional display name for the game.
     */
    private String name;

    /**
     * Timestamp when the game template was created.
     */
    private LocalDateTime createdAt;

    /**
     * List of exactly 5 milestones for this game.
     * Milestones are presented without dates to prevent cheating.
     */
    private List<MilestoneDto> milestones;
    
}