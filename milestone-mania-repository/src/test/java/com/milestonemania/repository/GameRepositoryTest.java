package com.milestonemania.repository;

import com.milestonemania.model.entity.Game;
import com.milestonemania.repository.config.DataJpaTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = DataJpaTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GameRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Test
    void findBySlug_ShouldReturnGameWhenExists() {
        // Given
        Game game = createGame("test-slug", "Test Game");
        entityManager.persistAndFlush(game);
        
        // When
        Optional<Game> result = gameRepository.findBySlug("test-slug");
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Game");
    }

    @Test
    void findBySlug_ShouldReturnEmptyWhenNotExists() {
        // When
        Optional<Game> result = gameRepository.findBySlug("non-existent");
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void existsBySlug_ShouldReturnTrueWhenExists() {
        // Given
        Game game = createGame("existing-slug", "Existing Game");
        entityManager.persistAndFlush(game);
        
        // When
        boolean exists = gameRepository.existsBySlug("existing-slug");
        
        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsBySlug_ShouldReturnFalseWhenNotExists() {
        // When
        boolean exists = gameRepository.existsBySlug("non-existent");
        
        // Then
        assertThat(exists).isFalse();
    }

    private Game createGame(String slug, String name) {
        Game game = new Game();
        game.setSlug(slug);
        game.setName(name);
        game.setCreatedAt(LocalDateTime.now());
        return game;
    }
}

