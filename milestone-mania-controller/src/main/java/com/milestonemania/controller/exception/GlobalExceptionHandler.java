package com.milestonemania.controller.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.milestonemania.controller.dto.response.ErrorResponse;
import com.milestonemania.controller.util.CorrelationIdUtil;
import com.milestonemania.service.api.exception.AttemptNotFoundException;
import com.milestonemania.service.api.exception.GameNotFoundException;
import com.milestonemania.service.api.exception.InsufficientMilestonesException;
import com.milestonemania.service.api.exception.InvalidAttemptStateException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Global exception handler for consistent error responses across all controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handle GameNotFoundException - 404 NOT_FOUND
   */
  @ExceptionHandler(GameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleGameNotFoundException(
      GameNotFoundException ex, HttpServletRequest request) {

    String correlationId = CorrelationIdUtil.generateCorrelationId();
    logger.warn("Game not found - CorrelationId: {}, Message: {}", correlationId, ex.getMessage());

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .correlationId(correlationId)
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .header("X-Correlation-ID", correlationId)
        .body(errorResponse);
  }

  /**
   * Handle AttemptNotFoundException - 404 NOT_FOUND
   */
  @ExceptionHandler(AttemptNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAttemptNotFoundException(
      AttemptNotFoundException ex, HttpServletRequest request) {

    String correlationId = CorrelationIdUtil.generateCorrelationId();
    logger.warn(
        "Attempt not found - CorrelationId: {}, Message: {}", correlationId, ex.getMessage());

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .correlationId(correlationId)
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .header("X-Correlation-ID", correlationId)
        .body(errorResponse);
  }

  /**
   * Handle InvalidAttemptStateException - 409 CONFLICT
   */
  @ExceptionHandler(InvalidAttemptStateException.class)
  public ResponseEntity<ErrorResponse> handleInvalidAttemptStateException(
      InvalidAttemptStateException ex, HttpServletRequest request) {

    String correlationId = CorrelationIdUtil.generateCorrelationId();
    logger.warn(
        "Invalid attempt state - CorrelationId: {}, Message: {}", correlationId, ex.getMessage());

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.CONFLICT.value())
            .error(HttpStatus.CONFLICT.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .correlationId(correlationId)
            .build();

    return ResponseEntity.status(HttpStatus.CONFLICT)
        .header("X-Correlation-ID", correlationId)
        .body(errorResponse);
  }

  /**
   * Handle InsufficientMilestonesException - 503 SERVICE_UNAVAILABLE
   */
  @ExceptionHandler(InsufficientMilestonesException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientMilestonesException(
      InsufficientMilestonesException ex, HttpServletRequest request) {

    String correlationId = CorrelationIdUtil.generateCorrelationId();
    logger.error(
        "Insufficient milestones - CorrelationId: {}, Message: {}", correlationId, ex.getMessage());

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.SERVICE_UNAVAILABLE.value())
            .error(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .correlationId(correlationId)
            .build();

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .header("X-Correlation-ID", correlationId)
        .body(errorResponse);
  }

  /**
   * Handle IllegalArgumentException - 400 BAD_REQUEST
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, HttpServletRequest request) {

    String correlationId = CorrelationIdUtil.generateCorrelationId();
    logger.warn(
        "Illegal argument - CorrelationId: {}, Message: {}", correlationId, ex.getMessage());

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .correlationId(correlationId)
            .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .header("X-Correlation-ID", correlationId)
        .body(errorResponse);
  }

  /**
   * Handle validation errors - 400 BAD_REQUEST
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    String correlationId = CorrelationIdUtil.generateCorrelationId();

    // Extract validation error messages
    StringBuilder messageBuilder = new StringBuilder("Validation failed: ");
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error ->
                messageBuilder
                    .append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; "));

    String message = messageBuilder.toString();
    logger.warn("Validation error - CorrelationId: {}, Message: {}", correlationId, message);

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(message)
            .path(request.getRequestURI())
            .correlationId(correlationId)
            .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .header("X-Correlation-ID", correlationId)
        .body(errorResponse);
  }

  /**
   * Handle generic exceptions - 500 INTERNAL_SERVER_ERROR
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, HttpServletRequest request, WebRequest webRequest) {

    String correlationId = CorrelationIdUtil.generateCorrelationId();
    logger.error(
        "Unexpected error - CorrelationId: {}, RequestURI: {}, Error: {}",
        correlationId,
        request.getRequestURI(),
        ex.getMessage(),
        ex);

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message("An unexpected error occurred. Please try again later.")
            .path(request.getRequestURI())
            .correlationId(correlationId)
            .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .header("X-Correlation-ID", correlationId)
        .body(errorResponse);
  }
}
