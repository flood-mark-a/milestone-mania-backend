package com.milestonemania.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Milestone information exposed to players.
 * <p>
 * Intentionally excludes actualDate to prevent cheating - players must
 * rely on their knowledge to order milestones chronologically.
 *
 * @author Milestone Mania Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneDto {

  /**
   * Unique identifier for the milestone.
   */
  private Long id;

  /**
   * Display title of the milestone event.
   */
  private String title;

  /**
   * Detailed description of the milestone event.
   */
  private String description;

  // Note: actualDate is intentionally excluded to prevent cheating
}
