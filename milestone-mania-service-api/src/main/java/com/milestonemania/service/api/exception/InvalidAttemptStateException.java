package com.milestonemania.service.api.exception;

import com.milestonemania.service.api.dto.AttemptStatus;

/**
 * Exception thrown when an operation is attempted on a game attempt 
 * that is in an invalid state for that operation.
 * <p>
 * This typically occurs when:
 * <ul>
 *   <li>Trying to submit an ordering for an already completed attempt</li>
 *   <li>Attempting to modify a completed attempt</li>
 *   <li>Operations that require IN_PROGRESS status on COMPLETED attempts</li>
 * </ul>
 * 
 * @author Milestone Mania Team
 * @version 1.0
 */
public class InvalidAttemptStateException extends RuntimeException {

    /**
     * Constructs a new InvalidAttemptStateException with the specified detail message.
     * 
     * @param message the detail message explaining the cause of the exception
     */
    public InvalidAttemptStateException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidAttemptStateException with a message and underlying cause.
     * 
     * @param message the detail message explaining the cause of the exception
     * @param cause the underlying cause of the exception
     */
    public InvalidAttemptStateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Convenience constructor for attempt already completed.
     * 
     * @param attemptId the attempt ID that is already completed
     * @return InvalidAttemptStateException with descriptive message
     */
    public static InvalidAttemptStateException alreadyCompleted(Long attemptId) {
        return new InvalidAttemptStateException(
            "Cannot submit ordering for attempt " + attemptId + " - already completed"
        );
    }

    /**
     * Convenience constructor for invalid state transition.
     * 
     * @param attemptId the attempt ID with invalid state
     * @param currentStatus the current status of the attempt
     * @param requiredStatus the required status for the operation
     * @return InvalidAttemptStateException with descriptive message
     */
    public static InvalidAttemptStateException invalidState(Long attemptId, 
                                                          AttemptStatus currentStatus, 
                                                          AttemptStatus requiredStatus) {
        return new InvalidAttemptStateException(
            String.format("Attempt %d is in %s state, but %s is required", 
                         attemptId, currentStatus, requiredStatus)
        );
    }
}