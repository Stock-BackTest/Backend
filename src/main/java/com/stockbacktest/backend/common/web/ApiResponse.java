package com.stockbacktest.backend.common.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;

@JsonInclude(Include.NON_NULL)
public record ApiResponse<T>(String message, T data, LocalDateTime timeStamp) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(null, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> empty() {
        return success(null);
    }
}
