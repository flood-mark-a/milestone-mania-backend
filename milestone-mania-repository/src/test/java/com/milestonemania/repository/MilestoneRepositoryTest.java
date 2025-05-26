package com.milestonemania.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import com.milestonemania.model.entity.Milestone;
import com.milestonemania.repository.config.DataJpaTestConfig;

@DataJpaTest
@ContextConfiguration(classes = DataJpaTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MilestoneRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private MilestoneRepository milestoneRepository;

  @Test
  void findRandomMilestones_ShouldReturnRequestedCount() {
    // Given
    createTestMilestones(10);

    // When
    List<Milestone> result = milestoneRepository.findRandomMilestones(5);

    // Then
    assertThat(result).hasSize(5);
    assertThat(result)
        .allSatisfy(
            milestone -> {
              assertThat(milestone.getId()).isNotNull();
              assertThat(milestone.getTitle()).isNotBlank();
            });
  }

  @Test
  void count_ShouldReturnCorrectCount() {
    // Given
    createTestMilestones(7);

    // When
    long count = milestoneRepository.count();

    // Then
    assertThat(count).isEqualTo(7);
  }

  @Test
  void findByTitleOrDescriptionContainingIgnoreCase_ShouldFindMatches() {
    // Given
    Milestone milestone1 = createMilestone("Moon Landing", "Apollo 11 lands on moon");
    Milestone milestone2 = createMilestone("Mars Mission", "First human mission to Mars");
    Milestone milestone3 = createMilestone("Ocean Discovery", "Deep sea exploration");

    entityManager.persistAndFlush(milestone1);
    entityManager.persistAndFlush(milestone2);
    entityManager.persistAndFlush(milestone3);

    // When
    List<Milestone> results =
        milestoneRepository.findByTitleOrDescriptionContainingIgnoreCase("moon");

    // Then
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getTitle()).isEqualTo("Moon Landing");
  }

  private void createTestMilestones(int count) {
    for (int i = 1; i <= count; i++) {
      Milestone milestone = createMilestone("Milestone " + i, "Description " + i);
      entityManager.persistAndFlush(milestone);
    }
  }

  private Milestone createMilestone(String title, String description) {
    Milestone milestone = new Milestone();
    milestone.setTitle(title);
    milestone.setDescription(description);
    milestone.setActualDate(LocalDate.now().minusDays((long) (Math.random() * 365)));
    return milestone;
  }
}
