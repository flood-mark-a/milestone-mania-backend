package com.milestonemania.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for milestone ordering submission responses.
 * <p>
 * Provides feedback on submission correctness and next steps for the player.
 * Includes static factory methods for creating success and failure responses.
 * 
 * @author Milestone Mania Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAttemptResponse {

    /**
     * Whether the submitted ordering was correct.
     */
    private boolean isCorrect;

    /**
     * Number of incorrect submissions made for this attempt.
     */
    private Integer incorrectCount;

    /**
     * Current attempt number (1-based).
     */
    private Integer attemptNumber;

    /**
     * Game slug for sharing functionality (populated on successful completion).
     */
    private String gameSlug;

    /**
     * Feedback message for the player.
     */
    private String message;

    /**
     * Creates a successful completion response.
     * 
     * @param gameSlug the game slug for sharing
     * @param attemptNumber the final attempt number
     * @param incorrectCount the number of previous incorrect attempts
     * @return SubmitAttemptResponse indicating success
     */
    public static SubmitAttemptResponse success(String gameSlug, Integer attemptNumber, Integer incorrectCount) {
        return SubmitAttemptResponse.builder()
                .isCorrect(true)
                .gameSlug(gameSlug)
                .attemptNumber(attemptNumber)
                .incorrectCount(incorrectCount)
                .message("Congratulations! You've successfully ordered all milestones chronologically.")
                .build();
    }

    /**
     * Creates a failure response for incorrect ordering.
     * 
     * @param attemptNumber the current attempt number
     * @param incorrectCount the updated count of incorrect attempts
     * @return SubmitAttemptResponse indicating failure
     */
    public static SubmitAttemptResponse failure(Integer attemptNumber, Integer incorrectCount) {
        return SubmitAttemptResponse.builder()
                .isCorrect(false)
                .attemptNumber(attemptNumber)
                .incorrectCount(incorrectCount)
                .message("The ordering is not quite right. Try again!")
                .build();
    }
}