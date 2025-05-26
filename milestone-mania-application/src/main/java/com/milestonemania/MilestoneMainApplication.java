package com.milestonemania;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for Milestone Mania.
 *
 * This application provides a timeline ordering game where players
 * arrange historical milestones in chronological order.
 */
@SpringBootApplication(scanBasePackages = "com.milestonemania")
public class MilestoneMainApplication {

  public static void main(String[] args) {
    SpringApplication.run(MilestoneMainApplication.class, args);
  }
}
