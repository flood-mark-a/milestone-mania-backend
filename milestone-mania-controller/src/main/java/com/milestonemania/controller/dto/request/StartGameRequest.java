package com.milestonemania.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for starting an existing game
 */
@Schema(description = "Request to start an existing game")
public class StartGameRequest {
    
    @Size(max = 100, message = "Player name must not exceed 100 characters")
    @Schema(description = "Optional player name", example = "Bob", maxLength = 100)
    private String playerName;
    
    // Default constructor
    public StartGameRequest() {}
    
    // Constructor with playerName
    public StartGameRequest(String playerName) {
        this.playerName = playerName;
    }
    
    // Getter
    public String getPlayerName() {
        return playerName;
    }
    
    // Setter
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
