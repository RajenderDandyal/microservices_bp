package com.invoice.service.exception;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceException extends RuntimeException {

//    private HttpStatus httpStatus;
//    private String errorMsg;
//    private List<String> errorDetails;
//
//    public InvoiceException(HttpStatus httpStatus, String errorMsg, List<String> errorDetails) {
//        super(errorMsg);
//        this.httpStatus = httpStatus;
//        this.errorMsg = errorMsg;
//        this.errorDetails = errorDetails;
//    }

    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Object errors; // for validation field errors

    public InvoiceException(int status, String message, String path, List<String> errors) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
