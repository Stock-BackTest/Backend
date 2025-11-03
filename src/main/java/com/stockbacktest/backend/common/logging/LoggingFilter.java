package com.stockbacktest.backend.common.logging;

import static java.nio.charset.StandardCharsets.UTF_8;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

  private static final AntPathMatcher matcher = new AntPathMatcher();
  private static final Set<String> SENSITIVE_HEADERS = Set.of(
      "authorization", "cookie", "set-cookie", "proxy-authorization"
  );
  public static final String CORRELATION_ID_KEY = "x-correlation-id";
  public static final String MDC_KEY = "correlationId";

  private final LoggingFilterProperties props;

  public LoggingFilter(LoggingFilterProperties props) {
    this.props = props;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    if (!props.enabled()) {
      return true;
    }
    String path = request.getRequestURI();
    for (String p : props.excludePatterns()) {
      if (matcher.match(p, path)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain)
      throws ServletException, IOException {

    ContentCachingRequestWrapper req = (request instanceof ContentCachingRequestWrapper)
        ? (ContentCachingRequestWrapper) request
        : new ContentCachingRequestWrapper(request, props.maxPayloadLength());

    ContentCachingResponseWrapper res = (response instanceof ContentCachingResponseWrapper)
        ? (ContentCachingResponseWrapper) response
        : new ContentCachingResponseWrapper(response);

    String correlationId = initCorrelationId(req);
    long started = System.nanoTime();
    try {
      chain.doFilter(req, res);
    } finally {
      long tookMs = Duration.ofNanos(System.nanoTime() - started).toMillis();
      try {
        LoggingFilterFormat event = buildEvent(req, res, tookMs, correlationId);

        log.info("http event: type={}, ts={}, meta={}, request={}, response={}",
            event.type(),
            event.ts(),
            event.meta(),
            event.request(),
            event.response());
      } catch (Throwable t) {
        log.warn("Failed to log http exchange", t);
      } finally {
        res.copyBodyToResponse();
        clearCorrelationId();
      }
    }
  }

  private LoggingFilterFormat buildEvent(ContentCachingRequestWrapper req,
      ContentCachingResponseWrapper res,
      long tookMs,
      String correlationId) {
    String path = req.getRequestURI();
    String query = props.logQueryString() ? req.getQueryString() : null;
    String uri = (query != null && !query.isBlank()) ? (path + "?" + query) : path;

    Map<String, String> reqHeaders = props.logHeaders() ? headersToMap(req) : null;
    Map<String, String> resHeaders = props.logHeaders() ? headersToMap(res) : null;

    String reqBody = (props.logBodies() && readableMediaType(req.getContentType()))
        ? trimBody(req.getContentAsByteArray())
        : null;
    String resBody = (props.logBodies() && readableMediaType(res.getContentType()))
        ? trimBody(res.getContentAsByteArray())
        : null;

    LoggingFilterFormat.Meta meta = new LoggingFilterFormat.Meta(
        correlationId,
        clientIp(req),
        tookMs,
        req.getProtocol(),
        headerOrNull(req, "User-Agent"),
        headerOrNull(req, "Referer")
    );

    LoggingFilterFormat.Request request = new LoggingFilterFormat.Request(
        req.getMethod(),
        uri,
        path,
        query,
        reqHeaders,
        requestContentType(req),
        requestContentLength(req),
        reqBody
    );

    LoggingFilterFormat.Response response = new LoggingFilterFormat.Response(
        res.getStatus(),
        resHeaders,
        responseContentType(res),
        responseContentLength(res),
        resBody
    );

    return new LoggingFilterFormat(
        "http",
        Instant.now(),
        meta,
        request,
        response
    );
  }

  private static String requestContentType(HttpServletRequest req) {
    String ct = req.getContentType();
    if (!StringUtils.hasText(ct)) {
      ct = req.getHeader("Content-Type");
    }
    return StringUtils.hasText(ct) ? ct : null;
  }

  private static String responseContentType(HttpServletResponse res) {
    String ct = res.getContentType();
    if (!StringUtils.hasText(ct)) {
      ct = res.getHeader("Content-Type");
    }
    return StringUtils.hasText(ct) ? ct : null;
  }

  private static Integer requestContentLength(ContentCachingRequestWrapper req) {
    int len = req.getContentLength();
    if (len >= 0) {
      return len;
    }
    byte[] b = req.getContentAsByteArray();
    return (b != null) ? b.length : null;
  }

  private static Integer responseContentLength(ContentCachingResponseWrapper res) {
    String h = res.getHeader("Content-Length");
    if (StringUtils.hasText(h)) {
      try {
        return Integer.parseInt(h);
      } catch (NumberFormatException ignored) {
      }
    }
    byte[] b = res.getContentAsByteArray();
    return (b != null) ? b.length : null;
  }


  private static String headerOrNull(HttpServletRequest req, String name) {
    String v = req.getHeader(name);
    return (v == null || v.isBlank()) ? null : v;
  }

  private static String clientIp(HttpServletRequest req) {
    String xff = req.getHeader("X-Forwarded-For");
    if (StringUtils.hasText(xff)) {
      return xff.split(",")[0].trim();
    }
    String realIp = req.getHeader("X-Real-IP");
    if (StringUtils.hasText(realIp)) {
      return realIp;
    }
    return req.getRemoteAddr();
  }

  private String trimBody(byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    int len = Math.min(bytes.length, props.maxPayloadLength());
    String body = new String(bytes, 0, len, UTF_8);
    if (bytes.length > len) {
      body += "...(truncated)";
    }
    return body;
  }

  private static boolean readableMediaType(String contentType) {
    if (!StringUtils.hasText(contentType)) {
      return false;
    }
    try {
      MediaType mt = MediaType.parseMediaType(contentType);
      if (MediaType.APPLICATION_JSON.includes(mt)) {
        return true;
      }
      if (MediaType.APPLICATION_XML.includes(mt)) {
        return true;
      }
      if (MediaType.TEXT_PLAIN.includes(mt)) {
        return true;
      }
      if (MediaType.TEXT_XML.includes(mt)) {
        return true;
      }
      if (MediaType.TEXT_HTML.includes(mt)) {
        return true;
      }
      if (MediaType.APPLICATION_FORM_URLENCODED.includes(mt)) {
        return true;
      }
      return "text".equalsIgnoreCase(mt.getType());
    } catch (Exception ignored) {
      return false;
    }
  }

  private static Map<String, String> headersToMap(HttpServletRequest req) {
    List<String> names = Collections.list(req.getHeaderNames());
    return names.stream().collect(Collectors.toMap(
        n -> n,
        n -> maskHeader(n, Collections.list(req.getHeaders(n))),
        (a, b) -> a,
        LinkedHashMap::new
    ));
  }

  private static Map<String, String> headersToMap(HttpServletResponse res) {
    return res.getHeaderNames().stream().collect(Collectors.toMap(
        n -> n,
        n -> maskHeader(n, new ArrayList<>(res.getHeaders(n))),
        (a, b) -> a,
        LinkedHashMap::new
    ));
  }

  private static String maskHeader(String name, List<String> values) {
    String lower = name.toLowerCase(Locale.ROOT);
    boolean sensitive = SENSITIVE_HEADERS.contains(lower);
    return values.stream()
        .map(v -> sensitive ? "***" : v)
        .collect(Collectors.joining("|"));
  }

  private static String initCorrelationId(HttpServletRequest req) {
    String existing = req.getHeader(CORRELATION_ID_KEY);
    String cid = StringUtils.hasText(existing) ? existing : UUID.randomUUID().toString();
    MDC.put(MDC_KEY, cid);
    return cid;
  }

  private static void clearCorrelationId() {
    MDC.remove(MDC_KEY);
  }
}
