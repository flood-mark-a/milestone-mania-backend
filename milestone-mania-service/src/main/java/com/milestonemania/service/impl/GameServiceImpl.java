package com.milestonemania.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milestonemania.model.entity.*;
import com.milestonemania.repository.*;
import com.milestonemania.service.api.GameService;
import com.milestonemania.service.api.dto.*;
import com.milestonemania.service.api.exception.*;
import com.milestonemania.service.util.SlugGenerator;

/**
 * Implementation of GameService for managing milestone ordering games.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

  private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);
  private static final int MILESTONES_PER_GAME = 5;
  private static final int MAX_SLUG_GENERATION_ATTEMPTS = 10;

  private final MilestoneRepository milestoneRepository;
  private final GameRepository gameRepository;
  private final GameMilestoneRepository gameMilestoneRepository;
  private final GameAttemptRepository gameAttemptRepository;
  private final SlugGenerator slugGenerator;

  public GameServiceImpl(
      MilestoneRepository milestoneRepository,
      GameRepository gameRepository,
      GameMilestoneRepository gameMilestoneRepository,
      GameAttemptRepository gameAttemptRepository,
      SlugGenerator slugGenerator) {
    this.milestoneRepository = milestoneRepository;
    this.gameRepository = gameRepository;
    this.gameMilestoneRepository = gameMilestoneRepository;
    this.gameAttemptRepository = gameAttemptRepository;
    this.slugGenerator = slugGenerator;
  }

  @Override
  public GameAttemptDto createNewGame(String playerName) {
    log.info("Creating new game for player: {}", playerName);

    // Check if we have enough milestones
    long milestoneCount = milestoneRepository.count();
    if (milestoneCount < MILESTONES_PER_GAME) {
      throw new InsufficientMilestonesException(
          String.format(
              "Need at least %d milestones, but only %d available",
              MILESTONES_PER_GAME, milestoneCount));
    }

    // Get random milestones
    List<Milestone> randomMilestones =
        milestoneRepository.findRandomMilestones(MILESTONES_PER_GAME);
    if (randomMilestones.size() < MILESTONES_PER_GAME) {
      throw new InsufficientMilestonesException(
          String.format(
              "Expected %d milestones, but only got %d",
              MILESTONES_PER_GAME, randomMilestones.size()));
    }

    // Sort milestones chronologically for correct order
    randomMilestones.sort(Comparator.comparing(Milestone::getActualDate));

    // Create game with unique slug
    Game game = createGameWithUniqueSlug(randomMilestones);

    // Create game attempt
    GameAttempt attempt = createGameAttempt(game, playerName);

    log.info(
        "Created new game {} with attempt {} for player {}",
        game.getSlug(),
        attempt.getId(),
        playerName);

    return mapToGameAttemptDto(attempt);
  }

  @Override
  @Transactional(readOnly = true)
  public GameAttemptDto startGameFromSlug(String slug, String playerName) {
    log.info("Starting game from slug {} for player: {}", slug, playerName);

    Game game =
        gameRepository
            .findBySlug(slug)
            .orElseThrow(() -> new GameNotFoundException("Game not found with slug: " + slug));

    GameAttempt attempt = createGameAttempt(game, playerName);

    log.info(
        "Started new attempt {} for existing game {} and player {}",
        attempt.getId(),
        slug,
        playerName);

    return mapToGameAttemptDto(attempt);
  }

  @Override
  public SubmitAttemptResponse submitAttempt(SubmitAttemptRequest request) {
    log.info(
        "Submitting attempt {} with {} milestone IDs",
        request.getAttemptId(),
        request.getOrderedMilestoneIds().size());

    try {
      GameAttempt attempt =
          gameAttemptRepository
              .findByIdAndStatus(
                  request.getAttemptId(), com.milestonemania.model.enums.AttemptStatus.IN_PROGRESS)
              .orElseThrow(
                  () ->
                      new AttemptNotFoundException(
                          "Active attempt not found with ID: " + request.getAttemptId()));

      // Validate milestone IDs belong to this game
      List<GameMilestone> correctGameMilestones =
          gameMilestoneRepository.findByGameOrderByCorrectOrder(attempt.getGame());

      validateMilestoneIds(request.getOrderedMilestoneIds(), correctGameMilestones);

      // Check if order is correct
      boolean isCorrect = isOrderCorrect(request.getOrderedMilestoneIds(), correctGameMilestones);

      String message = "Try again!!";

      if (isCorrect) {
        attempt.setStatus(com.milestonemania.model.enums.AttemptStatus.COMPLETED);
        attempt.setCompletedAt(LocalDateTime.now());
        message = "Congratulations!!";
        log.info(
            "Attempt {} completed successfully on attempt number {}",
            attempt.getId(),
            attempt.getAttemptCount());
      } else {
        attempt.setAttemptCount(attempt.getAttemptCount() + 1);
        log.info(
            "Attempt {} incorrect, now on attempt number {}",
            attempt.getId(),
            attempt.getAttemptCount());
      }

      gameAttemptRepository.save(attempt);

      return new SubmitAttemptResponse(
          isCorrect,
          isCorrect ? 0 : attempt.getAttemptCount() - 1,
          attempt.getAttemptCount(),
          attempt.getGame().getSlug(),
          message);

    } catch (OptimisticLockingFailureException e) {
      log.warn("Optimistic locking failure for attempt {}, retry required", request.getAttemptId());
      throw new InvalidAttemptStateException(
          "Attempt was modified by another request, please retry", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public GameDto getGameBySlug(String slug) {
    log.info("Retrieving game by slug: {}", slug);

    Game game =
        gameRepository
            .findBySlug(slug)
            .orElseThrow(() -> new GameNotFoundException("Game not found with slug: " + slug));

    return mapToGameDto(game);
  }

  private Game createGameWithUniqueSlug(List<Milestone> milestones) {
    for (int attempt = 0; attempt < MAX_SLUG_GENERATION_ATTEMPTS; attempt++) {
      try {
        String slug = slugGenerator.generateSlug();

        if (gameRepository.existsBySlug(slug)) {
          log.debug("Slug {} already exists, generating new one", slug);
          continue;
        }

        Game game = new Game();
        game.setSlug(slug);
        game.setName("Timeline Challenge: " + slug);
        game.setCreatedAt(LocalDateTime.now());

        game = gameRepository.save(game);

        // Create game milestones with correct order
        for (int i = 0; i < milestones.size(); i++) {
          GameMilestone gameMilestone = new GameMilestone();
          gameMilestone.setGame(game);
          gameMilestone.setMilestone(milestones.get(i));
          gameMilestone.setCorrectOrder(i + 1);
          gameMilestoneRepository.save(gameMilestone);
        }

        log.info("Created game with slug: {}", slug);
        return game;

      } catch (DataIntegrityViolationException e) {
        log.debug("Slug generation attempt {} failed due to collision", attempt + 1);
        if (attempt == MAX_SLUG_GENERATION_ATTEMPTS - 1) {
          throw new RuntimeException(
              "Failed to generate unique slug after " + MAX_SLUG_GENERATION_ATTEMPTS + " attempts",
              e);
        }
      }
    }

    throw new RuntimeException("Failed to generate unique slug");
  }

  private GameAttempt createGameAttempt(Game game, String playerName) {
    GameAttempt attempt = new GameAttempt();
    attempt.setGame(game);
    attempt.setPlayerName(playerName);
    attempt.setStatus(com.milestonemania.model.enums.AttemptStatus.IN_PROGRESS);
    attempt.setAttemptCount(1);
    attempt.setCreatedAt(LocalDateTime.now());
    attempt.setVersion(0L);

    return gameAttemptRepository.save(attempt);
  }

  private void validateMilestoneIds(
      List<Long> submittedIds, List<GameMilestone> correctGameMilestones) {
    List<Long> expectedIds =
        correctGameMilestones.stream()
            .map(gm -> gm.getMilestone().getId())
            .collect(Collectors.toList());

    if (!submittedIds.containsAll(expectedIds) || !expectedIds.containsAll(submittedIds)) {
      throw new InvalidAttemptStateException(
          "Submitted milestone IDs do not match the game milestones");
    }
  }

  private boolean isOrderCorrect(
      List<Long> submittedIds, List<GameMilestone> correctGameMilestones) {
    List<Long> correctOrder =
        correctGameMilestones.stream()
            .map(gm -> gm.getMilestone().getId())
            .collect(Collectors.toList());

    return submittedIds.equals(correctOrder);
  }

  private GameAttemptDto mapToGameAttemptDto(GameAttempt attempt) {
    List<GameMilestone> gameMilestones =
        gameMilestoneRepository.findByGameOrderByCorrectOrder(attempt.getGame());

    List<MilestoneDto> milestoneDtos =
        gameMilestones.stream()
            .map(
                gm ->
                    new MilestoneDto(
                        gm.getMilestone().getId(),
                        gm.getMilestone().getTitle(),
                        gm.getMilestone().getDescription()))
            .collect(Collectors.toList());

    return new GameAttemptDto(
        attempt.getId(),
        attempt.getGame().getSlug(),
        attempt.getPlayerName(),
        mapAttemptStatus(attempt.getStatus()),
        attempt.getAttemptCount(),
        attempt.getCreatedAt(),
        attempt.getCompletedAt(),
        milestoneDtos);
  }

  private GameDto mapToGameDto(Game game) {
    List<GameMilestone> gameMilestones =
        gameMilestoneRepository.findByGameOrderByCorrectOrder(game);

    List<MilestoneDto> milestoneDtos =
        gameMilestones.stream()
            .map(
                gm ->
                    new MilestoneDto(
                        gm.getMilestone().getId(),
                        gm.getMilestone().getTitle(),
                        gm.getMilestone().getDescription()))
            .collect(Collectors.toList());

    return new GameDto(
        game.getId(), game.getSlug(), game.getName(), game.getCreatedAt(), milestoneDtos);
  }

  private AttemptStatus mapAttemptStatus(
      com.milestonemania.model.enums.AttemptStatus attemptStatus) {
    return switch (attemptStatus) {
      case COMPLETED -> AttemptStatus.COMPLETED;
      case IN_PROGRESS -> AttemptStatus.IN_PROGRESS;
    };
  }
}
