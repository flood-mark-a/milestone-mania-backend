package com.milestonemania.service.api.dto;

/**
 * Enumeration representing the status of a game attempt in the service API layer.
 * <p>
 * This enum defines the possible states of a player's game attempt:
 * <ul>
 *   <li>IN_PROGRESS: Player is actively working on the game and can submit orderings</li>
 *   <li>COMPLETED: Player has successfully ordered all milestones chronologically</li>
 * </ul>
 *
 * Note: This is a service API enum independent of internal entity representations.
 * The service implementation layer is responsible for mapping between this enum
 * and any corresponding entity enums.
 *
 * @author Milestone Mania Team
 * @version 1.0
 */
public enum AttemptStatus {

  /**
   * The attempt is in progress and the player can continue submitting orderings.
   */
  IN_PROGRESS,

  /**
   * The attempt has been completed successfully with correct chronological ordering.
   */
  COMPLETED
}
