package com.milestonemania.controller.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Standard error response object for consistent API error handling
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response format")
public class ErrorResponse {

  @Schema(description = "Timestamp when error occurred", example = "2024-05-25T10:30:45.123Z")
  private String timestamp;

  @Schema(description = "HTTP status code", example = "404")
  private int status;

  @Schema(description = "HTTP status reason phrase", example = "Not Found")
  private String error;

  @Schema(
      description = "User-friendly error message",
      example = "Game not found with slug: adventure-timeline")
  private String message;

  @Schema(
      description = "Request path that caused the error",
      example = "/api/games/adventure-timeline")
  private String path;

  @Schema(description = "Unique correlation ID for debugging", example = "req-12345-abcde")
  private String correlationId;

  // Default constructor
  public ErrorResponse() {
    this.timestamp = Instant.now().toString();
  }

  // Constructor with essential fields
  public ErrorResponse(int status, String error, String message, String path) {
    this();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }

  // Full constructor
  public ErrorResponse(
      int status, String error, String message, String path, String correlationId) {
    this(status, error, message, path);
    this.correlationId = correlationId;
  }

  // Builder pattern for fluent construction
  public static ErrorResponseBuilder builder() {
    return new ErrorResponseBuilder();
  }

  // Getters
  public String getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public String getPath() {
    return path;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  // Setters for flexibility
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  // Builder class
  public static class ErrorResponseBuilder {
    private ErrorResponse errorResponse;

    private ErrorResponseBuilder() {
      this.errorResponse = new ErrorResponse();
    }

    public ErrorResponseBuilder status(int status) {
      errorResponse.status = status;
      return this;
    }

    public ErrorResponseBuilder error(String error) {
      errorResponse.error = error;
      return this;
    }

    public ErrorResponseBuilder message(String message) {
      errorResponse.message = message;
      return this;
    }

    public ErrorResponseBuilder path(String path) {
      errorResponse.path = path;
      return this;
    }

    public ErrorResponseBuilder correlationId(String correlationId) {
      errorResponse.correlationId = correlationId;
      return this;
    }

    public ErrorResponse build() {
      return errorResponse;
    }
  }
}
