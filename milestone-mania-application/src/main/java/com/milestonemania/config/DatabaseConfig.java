package com.milestonemania.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database configuration for the Milestone Mania application.
 */
@Configuration
@EntityScan(basePackages = "com.milestonemania.model")
@EnableJpaRepositories(basePackages = "com.milestonemania.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // Additional database-specific beans can be added here if needed
}

