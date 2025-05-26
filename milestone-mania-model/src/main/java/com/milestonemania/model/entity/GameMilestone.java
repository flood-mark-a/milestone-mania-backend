package com.milestonemania.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "game_milestones", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_game_milestone", columnNames = {"game_id", "milestone_id"}),
           @UniqueConstraint(name = "uk_game_order", columnNames = {"game_id", "correctOrder"})
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"game", "milestone"})
public class GameMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @NotNull
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id", nullable = false)
    @NotNull
    private Milestone milestone;

    @NotNull
    @Column(nullable = false)
    private Integer correctOrder;

    @Version
    private Long version;

    public GameMilestone(Game game, Milestone milestone, Integer correctOrder) {
        this.game = game;
        this.milestone = milestone;
        this.correctOrder = correctOrder;
    }
}

