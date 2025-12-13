package com.stockbacktest.backend.common.logging;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logging.http")
public record LoggingFilterProperties(
    boolean enabled,
    int maxPayloadLength,
    boolean logHeaders,
    boolean logBodies,
    boolean logQueryString,
    List<String> excludePatterns
) {

  public LoggingFilterProperties {
    if (maxPayloadLength < 0) {
      maxPayloadLength = 4 * 1024;
    }
    if (excludePatterns == null || excludePatterns.isEmpty()) {
      excludePatterns = List.of();
    }
  }
}
