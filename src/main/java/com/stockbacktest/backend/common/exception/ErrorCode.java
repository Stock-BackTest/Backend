package com.stockbacktest.backend.common.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
  ;
  private final HttpStatus status;
  private final String message;
  private final LogLevel logLevel;

  ErrorCode(HttpStatus status, String message, LogLevel logLevel) {
    this.status = status;
    this.message = message;
    this.logLevel = logLevel;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public LogLevel getLogLevel() {
    return logLevel;
  }

  public String getMessage() {
    return message;
  }
}
