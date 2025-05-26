package com.milestonemania.controller.util;

import java.util.UUID;

/**
 * Utility class for generating correlation IDs for request tracking
 */
public class CorrelationIdUtil {

  private static final String CORRELATION_ID_PREFIX = "req-";

  /**
   * Generates a unique correlation ID for request tracking
   * @return Unique correlation ID in format "req-{uuid}"
   */
  public static String generateCorrelationId() {
    return CORRELATION_ID_PREFIX + UUID.randomUUID().toString().substring(0, 8);
  }

  /**
   * Extracts correlation ID from request header or generates new one
   * @param headerValue Value from X-Correlation-ID header
   * @return Correlation ID (existing or newly generated)
   */
  public static String getOrGenerateCorrelationId(String headerValue) {
    if (headerValue != null && !headerValue.trim().isEmpty()) {
      return headerValue.trim();
    }
    return generateCorrelationId();
  }
}
