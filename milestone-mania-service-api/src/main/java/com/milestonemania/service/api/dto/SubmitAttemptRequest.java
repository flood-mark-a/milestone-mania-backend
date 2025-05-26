package com.milestonemania.service.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for milestone ordering submission requests.
 * <p>
 * Contains the player's proposed chronological ordering of milestones
 * for validation against the correct sequence.
 *
 * @author Milestone Mania Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAttemptRequest {

  /**
   * Unique identifier of the game attempt being submitted.
   */
  @NotNull(message = "Attempt ID is required")
  private Long attemptId;

  /**
   * List of milestone IDs in the player's proposed chronological order.
   * Must contain exactly 5 milestone IDs.
   */
  @NotNull(message = "Milestone order is required")
  @Size(min = 5, max = 5, message = "Exactly 5 milestones must be provided")
  private List<Long> orderedMilestoneIds;
}
