package com.stockbacktest.backend.common.logging;

import java.time.Instant;
import java.util.Map;

public record LoggingFilterFormat(
    String type,
    Instant ts,
    Meta meta,
    Request request,
    Response response
) {

  public record Meta(
      String correlationId,
      String ip,
      long tookMs,
      String protocol,
      String userAgent,
      String referer
  ) {

  }

  public record Request(
      String method,
      String uri,
      String path,
      String query,
      Map<String, String> headers,
      String contentType,
      Integer contentLength,
      String body
  ) {

  }

  public record Response(
      int status,
      Map<String, String> headers,
      String contentType,
      Integer contentLength,
      String body
  ) {

  }
}
