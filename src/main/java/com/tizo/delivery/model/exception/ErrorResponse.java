package com.tizo.delivery.model.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final HttpStatus error;
    private final String message;
    private final String path;

    public ErrorResponse(HttpStatus error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = error.value();
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public HttpStatus getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}