package com.milestonemania.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;

/**
 * OpenAPI/Swagger configuration for Milestone Mania API documentation
 */
@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Milestone Mania API",
            version = "1.0.0",
            description =
                "REST API for the Milestone Mania timeline ordering game. "
                    + "Players order historical milestones chronologically to complete challenges.",
            contact = @Contact(name = "Milestone Mania Team", email = "support@milestonemania.com"),
            license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")),
    servers = {
      @Server(description = "Development Server", url = "http://localhost:8080"),
      @Server(description = "Production Server", url = "https://api.milestonemania.com")
    })
public class OpenApiConfig {

  /**
   * Configure OpenAPI with common error response examples
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(
            new Components()
                .addResponses(
                    "BadRequest",
                    createErrorResponse(
                        "Bad Request",
                        "Invalid request parameters or validation errors",
                        400,
                        "Bad Request",
                        "Validation failed: milestoneIds - must not be empty; ",
                        "/api/games/attempts/submit"))
                .addResponses(
                    "NotFound",
                    createErrorResponse(
                        "Not Found",
                        "Requested resource not found",
                        404,
                        "Not Found",
                        "Game not found with slug: invalid-game-slug",
                        "/api/games/invalid-game-slug"))
                .addResponses(
                    "Conflict",
                    createErrorResponse(
                        "Conflict",
                        "Request conflicts with current resource state",
                        409,
                        "Conflict",
                        "Attempt is already completed and cannot be modified",
                        "/api/games/attempts/submit"))
                .addResponses(
                    "ServiceUnavailable",
                    createErrorResponse(
                        "Service Unavailable",
                        "Service temporarily unavailable",
                        503,
                        "Service Unavailable",
                        "Insufficient milestones available to create a new game",
                        "/api/games/new"))
                .addResponses(
                    "InternalServerError",
                    createErrorResponse(
                        "Internal Server Error",
                        "Unexpected server error occurred",
                        500,
                        "Internal Server Error",
                        "An unexpected error occurred. Please try again later.",
                        "/api/games/new")));
  }

  /**
   * Create a standardized error response for OpenAPI documentation
   */
  private ApiResponse createErrorResponse(
      String description, String summary, int status, String error, String message, String path) {
    Example errorExample = new Example();
    errorExample.setSummary(summary);
    errorExample.setValue(
        String.format(
            """
                {
                    "timestamp": "2024-05-25T10:30:45.123Z",
                    "status": %d,
                    "error": "%s",
                    "message": "%s",
                    "path": "%s",
                    "correlationId": "req-12345-abcde"
                }
                """,
            status, error, message, path));

    MediaType mediaType = new MediaType();
    mediaType.addExamples("default", errorExample);

    Content content = new Content();
    content.addMediaType("application/json", mediaType);

    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setDescription(description);
    apiResponse.setContent(content);

    return apiResponse;
  }
}
