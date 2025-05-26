package com.milestonemania.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new game
 */
@Schema(description = "Request to create a new game")
public class CreateGameRequest {

  @Size(max = 100, message = "Player name must not exceed 100 characters")
  @Schema(description = "Optional player name", example = "Alice", maxLength = 100)
  private String playerName;

  // Default constructor
  public CreateGameRequest() {}

  // Constructor with playerName
  public CreateGameRequest(String playerName) {
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
