package com.yourcompany.user.service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Object errors; // for validation field errors

    public ApiError(int status, String message, String path, Object errors) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    // getters & setters
}

