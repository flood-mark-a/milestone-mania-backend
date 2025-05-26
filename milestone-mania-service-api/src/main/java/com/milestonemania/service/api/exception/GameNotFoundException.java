package com.milestonemania.service.api.exception;

/**
 * Exception thrown when a requested game cannot be found by its slug.
 * <p>
 * This typically occurs when:
 * <ul>
 *   <li>A user tries to access a game with an invalid or non-existent slug</li>
 *   <li>A game has been deleted or is no longer available</li>
 * </ul>
 *
 * @author Milestone Mania Team
 * @version 1.0
 */
public class GameNotFoundException extends RuntimeException {

  /**
   * Constructs a new GameNotFoundException with the specified detail message.
   *
   * @param message the detail message explaining the cause of the exception
   */
  public GameNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new GameNotFoundException with a message and underlying cause.
   *
   * @param message the detail message explaining the cause of the exception
   * @param cause the underlying cause of the exception
   */
  public GameNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Convenience constructor for missing game by slug.
   *
   * @param slug the slug that was not found
   * @return GameNotFoundException with descriptive message
   */
  public static GameNotFoundException forSlug(String slug) {
    return new GameNotFoundException("Game not found with slug: " + slug);
  }
}
