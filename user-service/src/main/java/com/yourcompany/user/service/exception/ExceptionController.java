package com.yourcompany.user.service.exception;


import com.yourcompany.user.service.dto.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleInvoiceException(ApiException invoiceException) {

        ApiError apiError = new ApiError(invoiceException.getStatus(), invoiceException.getMessage(), invoiceException.getPath(), invoiceException.getErrors());
        return ResponseEntity.status(invoiceException.getStatus()).body(apiError);
    }

}
