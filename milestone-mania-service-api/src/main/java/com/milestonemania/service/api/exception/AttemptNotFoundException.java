package com.milestonemania.service.api.exception;

/**
 * Exception thrown when a requested game attempt cannot be found by its ID.
 * <p>
 * This typically occurs when:
 * <ul>
 *   <li>A user tries to submit an ordering for a non-existent attempt ID</li>
 *   <li>An attempt has been deleted or expired</li>
 *   <li>The attempt ID is malformed or invalid</li>
 * </ul>
 *
 * @author Milestone Mania Team
 * @version 1.0
 */
public class AttemptNotFoundException extends RuntimeException {

  /**
   * Constructs a new AttemptNotFoundException with the specified detail message.
   *
   * @param message the detail message explaining the cause of the exception
   */
  public AttemptNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new AttemptNotFoundException with a message and underlying cause.
   *
   * @param message the detail message explaining the cause of the exception
   * @param cause the underlying cause of the exception
   */
  public AttemptNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Convenience constructor for missing attempt by ID.
   *
   * @param attemptId the attempt ID that was not found
   * @return AttemptNotFoundException with descriptive message
   */
  public static AttemptNotFoundException forAttemptId(Long attemptId) {
    return new AttemptNotFoundException("Game attempt not found with ID: " + attemptId);
  }
}
