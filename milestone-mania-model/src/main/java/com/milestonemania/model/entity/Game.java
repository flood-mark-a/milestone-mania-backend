package com.milestonemania.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
    name = "games",
    indexes = {@Index(name = "idx_game_slug", columnList = "slug", unique = true)})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"gameMilestones", "gameAttempts"})
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @NotBlank
  @Size(max = 100)
  @Column(nullable = false, unique = true, length = 100)
  private String slug;

  @Size(max = 255)
  @Column(length = 255)
  private String name;

  @NotNull
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Version private Long version;

  @OneToMany(
      mappedBy = "game",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private List<GameMilestone> gameMilestones = new ArrayList<>();

  @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
  private List<GameAttempt> gameAttempts = new ArrayList<>();

  public Game(String slug, String name, LocalDateTime createdAt) {
    this.slug = slug;
    this.name = name;
    this.createdAt = createdAt;
  }

  public Game(String slug, LocalDateTime createdAt) {
    this.slug = slug;
    this.createdAt = createdAt;
  }

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }
}
