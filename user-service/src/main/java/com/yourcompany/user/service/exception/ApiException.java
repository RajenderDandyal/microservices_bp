package com.yourcompany.user.service.exception;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiException extends RuntimeException {

    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Object errors; // for validation field errors

    public ApiException(int status, String message, String path, Object errors) {
        this.status = status;
        this.message = message;
        this.path = path;

        if (errors instanceof List) {
            this.errors = errors;
        } else {
            this.errors = List.of(errors.toString());
        }
        this.timestamp = LocalDateTime.now();
    }
}
