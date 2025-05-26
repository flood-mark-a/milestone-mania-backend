package com.milestonemania.controller;

import com.milestonemania.controller.dto.request.CreateGameRequest;
import com.milestonemania.controller.dto.request.StartGameRequest;
import com.milestonemania.controller.util.CorrelationIdUtil;
import com.milestonemania.service.api.GameService;
import com.milestonemania.service.api.dto.GameAttemptDto;
import com.milestonemania.service.api.dto.GameDto;
import com.milestonemania.service.api.dto.SubmitAttemptRequest;
import com.milestonemania.service.api.dto.SubmitAttemptResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing timeline ordering game operations
 */
@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Game Controller", description = "APIs for managing timeline ordering games")
public class GameController {
    
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    
    private final GameService gameService;
    
    /**
     * Constructor with dependency injection
     * @param gameService Service for game operations
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    
    /**
     * Create a new game with optional player name
     */
    @PostMapping
    @Operation(
        summary = "Create a new game",
        description = "Creates a new timeline ordering game with a unique slug and optional player name"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Game created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GameAttemptDto.class),
                examples = @ExampleObject(
                    name = "New Game Response",
                    value = """
                    {
                        "attemptId": 123,
                        "gameSlug": "adventure-timeline-abc123",
                        "playerName": "Alice",
                        "status": "IN_PROGRESS",
                        "attemptCount": 1,
                        "createdAt": "2024-05-25T10:30:45",
                        "completedAt": null,
                        "milestones": [
                            {
                                "id": 1,
                                "title": "Moon Landing",
                                "description": "First human landing on the Moon"
                            }
                        ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Insufficient milestones available",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<GameAttemptDto> createNewGame(
            @RequestBody @Valid CreateGameRequest request,
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        
        correlationId = CorrelationIdUtil.getOrGenerateCorrelationId(correlationId);
        logger.info("Creating new game - CorrelationId: {}, PlayerName: {}", 
                   correlationId, request.getPlayerName());
        
        GameAttemptDto gameAttempt = gameService.createNewGame(request.getPlayerName());
        
        logger.info("New game created - CorrelationId: {}, GameSlug: {}, AttemptId: {}", 
                   correlationId, gameAttempt.getGameSlug(), gameAttempt.getAttemptId());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Correlation-ID", correlationId)
                .body(gameAttempt);
    }
    
    /**
     * Start an existing game by slug with optional player name
     */
    @PostMapping("/{slug}/start")
    @Operation(
        summary = "Start an existing game",
        description = "Start playing an existing game by its slug with optional player name"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Game started successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GameAttemptDto.class),
                examples = @ExampleObject(
                    name = "Started Game Response",
                    value = """
                    {
                        "attemptId": 124,
                        "gameSlug": "adventure-timeline-abc123",
                        "playerName": "Bob",
                        "status": "IN_PROGRESS",
                        "attemptCount": 1,
                        "createdAt": "2024-05-25T10:35:20",
                        "completedAt": null,
                        "milestones": [
                            {
                                "id": 1,
                                "title": "Moon Landing",
                                "description": "First human landing on the Moon"
                            }
                        ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data or slug format",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Game not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<GameAttemptDto> startExistingGame(
            @PathVariable 
            @Pattern(regexp = "^[a-zA-Z0-9\\-]{3,50}$", message = "Invalid slug format")
            @Parameter(description = "Game slug identifier", example = "adventure-timeline-abc123")
            String slug,
            @RequestBody @Valid StartGameRequest request,
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        
        correlationId = CorrelationIdUtil.getOrGenerateCorrelationId(correlationId);
        logger.info("Starting existing game - CorrelationId: {}, Slug: {}, PlayerName: {}", 
                   correlationId, slug, request.getPlayerName());
        
        GameAttemptDto gameAttempt = gameService.startGameFromSlug(slug, request.getPlayerName());
        
        logger.info("Game started - CorrelationId: {}, Slug: {}, AttemptId: {}", 
                   correlationId, slug, gameAttempt.getAttemptId());
        
        return ResponseEntity.ok()
                .header("X-Correlation-ID", correlationId)
                .body(gameAttempt);
    }
    
    /**
     * Submit an attempt for timeline ordering
     */
    @PostMapping("/attempts/submit")
    @Operation(
        summary = "Submit timeline attempt",
        description = "Submit an ordered list of milestone IDs for validation"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Attempt submitted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SubmitAttemptResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Correct Answer",
                        value = """
                        {
                            "isCorrect": true,
                            "incorrectCount": 0,
                            "attemptNumber": 3,
                            "gameSlug": "adventure-timeline-abc123"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Incorrect Answer",
                        value = """
                        {
                            "isCorrect": false,
                            "incorrectCount": 2,
                            "attemptNumber": 3,
                            "gameSlug": "adventure-timeline-abc123"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data or validation errors",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Attempt not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Invalid attempt state (already completed)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<SubmitAttemptResponse> submitAttempt(
            @RequestBody @Valid SubmitAttemptRequest request,
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        
        correlationId = CorrelationIdUtil.getOrGenerateCorrelationId(correlationId);
        logger.info("Submitting attempt - CorrelationId: {}, AttemptId: {}, MilestoneIds: {}", 
                   correlationId, request.getAttemptId(), request.getOrderedMilestoneIds());
        
        SubmitAttemptResponse response = gameService.submitAttempt(request);
        
        logger.info("Attempt submitted - CorrelationId: {}, IsCorrect: {}, AttemptNumber: {}", 
                   correlationId, response.isCorrect(), response.getAttemptNumber());
        
        return ResponseEntity.ok()
                .header("X-Correlation-ID", correlationId)
                .body(response);
    }
    
    /**
     * Get game information by slug
     */
    @GetMapping("/{slug}")
    @Operation(
        summary = "Get game information",
        description = "Retrieve game information including milestones by slug"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Game information retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GameDto.class),
                examples = @ExampleObject(
                    name = "Game Information",
                    value = """
                    {
                        "id": 1,
                        "slug": "adventure-timeline-abc123",
                        "name": "Adventure Timeline",
                        "createdAt": "2024-05-25T09:00:00",
                        "milestones": [
                            {
                                "id": 1,
                                "title": "Moon Landing",
                                "description": "First human landing on the Moon"
                            },
                            {
                                "id": 2,
                                "title": "Internet Creation",
                                "description": "Birth of the World Wide Web"
                            }
                        ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid slug format",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Game not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.milestonemania.controller.dto.response.ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<GameDto> getGameBySlug(
            @PathVariable 
            @Pattern(regexp = "^[a-zA-Z0-9\\-]{3,50}$", message = "Invalid slug format")
            @Parameter(description = "Game slug identifier", example = "adventure-timeline-abc123")
            String slug,
            @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
        
        correlationId = CorrelationIdUtil.getOrGenerateCorrelationId(correlationId);
        logger.info("Getting game by slug - CorrelationId: {}, Slug: {}", correlationId, slug);
        
        GameDto game = gameService.getGameBySlug(slug);
        
        logger.info("Game retrieved - CorrelationId: {}, Slug: {}, GameId: {}", 
                   correlationId, slug, game.getId());
        
        return ResponseEntity.ok()
                .header("X-Correlation-ID", correlationId)
                .body(game);
    }
}