package com.user.service.exception;


import com.user.service.dto.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiError> handleInvoiceException(UserException invoiceException) {

        ApiError invoiceError = new ApiError(invoiceException.getStatus(), invoiceException.getMessage(), invoiceException.getPath(), invoiceException.getErrors());
        return ResponseEntity.status(invoiceException.getStatus()).body(invoiceError);
    }

}
