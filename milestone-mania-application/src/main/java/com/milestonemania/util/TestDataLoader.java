// Add this to your application module
// src/main/java/com/milestonemania/config/TestDataLoader.java

package com.milestonemania.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.milestonemania.model.entity.Milestone;
import com.milestonemania.repository.MilestoneRepository;

/**
 * Loads test data on application startup for development and testing.
 * Only runs on 'dev' profile to avoid polluting production.
 */
@Configuration
public class TestDataLoader {

  private static final Logger log = LoggerFactory.getLogger(TestDataLoader.class);

  @Bean
  public ApplicationRunner loadTestData(MilestoneRepository milestoneRepository) {
    return args -> {
      if (milestoneRepository.count() > 0) {
        log.info("Test data already exists, skipping data load");
        return;
      }

      log.info("Loading test milestone data...");

      List<Milestone> testMilestones = createTestMilestones();
      milestoneRepository.saveAll(testMilestones);

      log.info("Successfully loaded {} test milestones", testMilestones.size());
    };
  }

  private List<Milestone> createTestMilestones() {
    return Arrays.asList(
        // 2025 Events
        createMilestone(
            "ChatGPT-4 Turbo Released",
            "OpenAI releases ChatGPT-4 Turbo with improved capabilities",
            LocalDate.of(2025, 3, 15)),
        createMilestone(
            "Apple Vision Pro 2 Launch",
            "Apple announces second generation Vision Pro with enhanced features",
            LocalDate.of(2025, 6, 10)),
        createMilestone(
            "Tesla Model 2 Unveiled",
            "Tesla unveils affordable $25,000 Model 2 electric vehicle",
            LocalDate.of(2025, 9, 22)),

        // 2024 Events
        createMilestone(
            "2024 Paris Olympics",
            "Summer Olympics held in Paris, France",
            LocalDate.of(2024, 7, 26)),
        createMilestone(
            "Apple Vision Pro Launch",
            "Apple releases its first mixed reality headset",
            LocalDate.of(2024, 2, 2)),
        createMilestone(
            "OpenAI GPT-4 Turbo",
            "OpenAI releases GPT-4 Turbo with 128k context window",
            LocalDate.of(2024, 4, 9)),
        createMilestone(
            "Meta Llama 3 Release",
            "Meta releases open-source Llama 3 language model",
            LocalDate.of(2024, 4, 18)),
        createMilestone(
            "Tesla Cybertruck Delivery",
            "Tesla begins mass delivery of Cybertruck",
            LocalDate.of(2024, 11, 30)),
        createMilestone(
            "SpaceX Starship Test Flight",
            "SpaceX conducts successful Starship orbital test",
            LocalDate.of(2024, 3, 14)),
        createMilestone(
            "Microsoft Copilot for Microsoft 365",
            "Microsoft integrates AI assistant across Office suite",
            LocalDate.of(2024, 1, 15)),

        // 2023 Events
        createMilestone(
            "ChatGPT Reaches 100M Users",
            "ChatGPT becomes fastest-growing app in history",
            LocalDate.of(2023, 1, 31)),
        createMilestone(
            "Threads App Launch",
            "Meta launches Twitter competitor Threads",
            LocalDate.of(2023, 7, 5)),
        createMilestone(
            "Google Bard Released",
            "Google launches AI chatbot Bard to compete with ChatGPT",
            LocalDate.of(2023, 3, 21)),
        createMilestone(
            "Silicon Valley Bank Collapse",
            "SVB collapses, triggering banking sector concerns",
            LocalDate.of(2023, 3, 10)),
        createMilestone(
            "Twitter Rebrand to X", "Elon Musk rebrands Twitter to X", LocalDate.of(2023, 7, 23)),
        createMilestone(
            "OpenAI GPT-4 Launch",
            "OpenAI releases GPT-4 with multimodal capabilities",
            LocalDate.of(2023, 3, 14)),
        createMilestone(
            "Apple iPhone 15 Launch",
            "Apple releases iPhone 15 with USB-C port",
            LocalDate.of(2023, 9, 22)),
        createMilestone(
            "Microsoft Bing AI",
            "Microsoft integrates ChatGPT into Bing search",
            LocalDate.of(2023, 2, 7)),

        // 2022 Events
        createMilestone(
            "Russia Invades Ukraine",
            "Russia begins full-scale invasion of Ukraine",
            LocalDate.of(2022, 2, 24)),
        createMilestone(
            "Elon Musk Buys Twitter",
            "Elon Musk completes $44 billion Twitter acquisition",
            LocalDate.of(2022, 10, 27)),
        createMilestone(
            "Queen Elizabeth II Dies",
            "Britain's longest-reigning monarch passes away",
            LocalDate.of(2022, 9, 8)),
        createMilestone(
            "FIFA World Cup Qatar",
            "World Cup held in Qatar with Argentina victory",
            LocalDate.of(2022, 11, 20)),
        createMilestone(
            "ChatGPT Public Release",
            "OpenAI releases ChatGPT to the public",
            LocalDate.of(2022, 11, 30)),
        createMilestone(
            "iPhone 14 Launch",
            "Apple releases iPhone 14 series with satellite connectivity",
            LocalDate.of(2022, 9, 16)),
        createMilestone(
            "Tesla Stock Split", "Tesla conducts 3-for-1 stock split", LocalDate.of(2022, 8, 25)),
        createMilestone(
            "NASA JWST First Images",
            "James Webb Space Telescope releases first full-color images",
            LocalDate.of(2022, 7, 12)),
        createMilestone(
            "Netflix Loses Subscribers",
            "Netflix reports first subscriber loss in a decade",
            LocalDate.of(2022, 4, 19)),

        // 2021 Events
        createMilestone(
            "Biden Inauguration",
            "Joe Biden inaugurated as 46th US President",
            LocalDate.of(2021, 1, 20)),
        createMilestone(
            "GameStop Stock Surge",
            "GameStop stock surges due to Reddit-driven trading",
            LocalDate.of(2021, 1, 28)),
        createMilestone(
            "Suez Canal Blockage",
            "Ever Given container ship blocks Suez Canal",
            LocalDate.of(2021, 3, 23)),
        createMilestone(
            "SpaceX Crew Dragon Success",
            "SpaceX successfully launches astronauts to ISS",
            LocalDate.of(2021, 4, 23)),
        createMilestone(
            "COVID-19 Vaccine Rollout",
            "Global COVID-19 vaccination campaign begins",
            LocalDate.of(2021, 1, 8)),
        createMilestone(
            "Bitcoin Hits $60K",
            "Bitcoin reaches all-time high of $60,000",
            LocalDate.of(2021, 3, 13)),
        createMilestone(
            "Tokyo Olympics", "Delayed 2020 Olympics held in Tokyo", LocalDate.of(2021, 7, 23)),
        createMilestone(
            "Instagram Reels Launch",
            "Instagram launches Reels to compete with TikTok",
            LocalDate.of(2021, 8, 5)),
        createMilestone(
            "Apple M1 Chip",
            "Apple releases MacBooks with custom M1 silicon",
            LocalDate.of(2021, 11, 10)),
        createMilestone(
            "GitHub Copilot Launch",
            "AI pair programming assistant becomes available",
            LocalDate.of(2021, 6, 29)),
        createMilestone(
            "NFT Market Boom",
            "Non-fungible tokens reach mainstream popularity",
            LocalDate.of(2021, 3, 11)),
        createMilestone(
            "Clubhouse App Popularity",
            "Audio-based social network gains massive traction",
            LocalDate.of(2021, 2, 1)),
        createMilestone(
            "Windows 11 Release",
            "Microsoft releases Windows 11 operating system",
            LocalDate.of(2021, 10, 5)),
        createMilestone(
            "Epic vs Apple Trial",
            "Epic Games sues Apple over App Store policies",
            LocalDate.of(2021, 5, 3)),
        createMilestone(
            "Robinhood IPO", "Popular trading app goes public", LocalDate.of(2021, 7, 29)),
        createMilestone(
            "Dogecoin Surge",
            "Dogecoin cryptocurrency reaches all-time high",
            LocalDate.of(2021, 5, 8)),
        createMilestone(
            "Signal App Downloads Surge",
            "Privacy-focused messaging app gains millions of users",
            LocalDate.of(2021, 1, 12)),
        createMilestone(
            "Mars Perseverance Landing",
            "NASA's Perseverance rover lands on Mars",
            LocalDate.of(2021, 2, 18)),
        createMilestone(
            "Ingenuity Mars Helicopter",
            "First powered flight on another planet",
            LocalDate.of(2021, 4, 19)),
        createMilestone(
            "Blue Origin Space Tourism",
            "Jeff Bezos flies to space on Blue Origin",
            LocalDate.of(2021, 7, 20)),
        createMilestone(
            "Virgin Galactic Space Flight",
            "Richard Branson reaches space on Virgin Galactic",
            LocalDate.of(2021, 7, 11)),

        // 2020 Events
        createMilestone(
            "WHO Declares COVID-19 Pandemic",
            "World Health Organization declares COVID-19 a pandemic",
            LocalDate.of(2020, 3, 11)),
        createMilestone(
            "First COVID-19 Lockdowns",
            "Global lockdowns begin to combat COVID-19 spread",
            LocalDate.of(2020, 3, 15)),
        createMilestone(
            "George Floyd Protests",
            "Nationwide protests following George Floyd's death",
            LocalDate.of(2020, 5, 25)),
        createMilestone(
            "TikTok Nearly Banned",
            "US government threatens to ban TikTok",
            LocalDate.of(2020, 8, 6)),
        createMilestone(
            "Zoom IPO Success",
            "Zoom becomes most valuable video conferencing company",
            LocalDate.of(2020, 4, 18)),
        createMilestone(
            "Tesla Joins S&P 500", "Tesla added to S&P 500 index", LocalDate.of(2020, 12, 21)),
        createMilestone(
            "US Election 2020",
            "Joe Biden wins US Presidential election",
            LocalDate.of(2020, 11, 7)),
        createMilestone(
            "SpaceX Dragon Crew",
            "First commercial crew mission to International Space Station",
            LocalDate.of(2020, 5, 30)),
        createMilestone(
            "PlayStation 5 Launch",
            "Sony releases PlayStation 5 console",
            LocalDate.of(2020, 11, 12)),
        createMilestone(
            "iPhone 12 Launch",
            "Apple releases iPhone 12 with 5G capability",
            LocalDate.of(2020, 10, 23)));
  }

  private Milestone createMilestone(String title, String description, LocalDate date) {
    Milestone milestone = new Milestone();
    milestone.setTitle(title);
    milestone.setDescription(description);
    milestone.setActualDate(date);
    return milestone;
  }
}
