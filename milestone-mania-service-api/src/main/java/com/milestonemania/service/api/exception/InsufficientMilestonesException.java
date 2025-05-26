package com.milestonemania.service.api.exception;

/**
 * Exception thrown when there are insufficient milestones available 
 * in the system to create a new game.
 * <p>
 * This occurs when:
 * <ul>
 *   <li>Fewer than 5 milestones exist in the milestone repository</li>
 *   <li>The system cannot select enough unique milestones for a game</li>
 *   <li>Database connectivity issues prevent milestone retrieval</li>
 * </ul>
 * 
 * Games require exactly 5 milestones, so this exception prevents
 * the creation of incomplete games.
 * 
 * @author Milestone Mania Team
 * @version 1.0
 */
public class InsufficientMilestonesException extends RuntimeException {

    /**
     * Constructs a new InsufficientMilestonesException with the specified detail message.
     * 
     * @param message the detail message explaining the cause of the exception
     */
    public InsufficientMilestonesException(String message) {
        super(message);
    }

    /**
     * Constructs a new InsufficientMilestonesException with a message and underlying cause.
     * 
     * @param message the detail message explaining the cause of the exception
     * @param cause the underlying cause of the exception
     */
    public InsufficientMilestonesException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Convenience constructor for insufficient milestone count.
     * 
     * @param availableCount the number of milestones currently available
     * @param requiredCount the number of milestones required (typically 5)
     * @return InsufficientMilestonesException with descriptive message
     */
    public static InsufficientMilestonesException notEnoughMilestones(int availableCount, int requiredCount) {
        return new InsufficientMilestonesException(
            String.format("Insufficient milestones available: found %d, need %d", 
                         availableCount, requiredCount)
        );
    }
}