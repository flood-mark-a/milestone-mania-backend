package com.milestonemania.model.entity;

import java.time.LocalDateTime;

import com.milestonemania.model.enums.AttemptStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
    name = "game_attempts",
    indexes = {@Index(name = "idx_attempt_status_created", columnList = "status, createdAt")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"game"})
public class GameAttempt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id", nullable = false)
  @NotNull
  private Game game;

  @Size(max = 100)
  @Column(length = 100)
  private String playerName;

  @Enumerated(EnumType.STRING)
  @NotNull
  @Column(nullable = false)
  private AttemptStatus status;

  private Integer score;

  @NotNull
  @Column(nullable = false)
  private LocalDateTime createdAt;

  private LocalDateTime completedAt;

  @NotNull
  @Column(nullable = false)
  private Integer attemptCount = 1;

  @Version private Long version;

  public GameAttempt(Game game, String playerName, AttemptStatus status, LocalDateTime createdAt) {
    this.game = game;
    this.playerName = playerName;
    this.status = status;
    this.createdAt = createdAt;
    this.attemptCount = 1;
  }

  public GameAttempt(Game game, AttemptStatus status, LocalDateTime createdAt) {
    this.game = game;
    this.status = status;
    this.createdAt = createdAt;
    this.attemptCount = 1;
  }

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
    if (attemptCount == null) {
      attemptCount = 1;
    }
  }
}
