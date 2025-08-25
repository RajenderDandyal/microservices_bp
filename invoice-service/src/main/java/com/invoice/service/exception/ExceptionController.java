package com.invoice.service.exception;


import com.invoice.service.dto.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(InvoiceException.class)
    public ResponseEntity<ApiError> handleInvoiceException(InvoiceException invoiceException) {

        ApiError invoiceError = new ApiError(invoiceException.getStatus(), invoiceException.getMessage(), invoiceException.getPath(), invoiceException.getErrors());
        return ResponseEntity.status(invoiceException.getStatus()).body(invoiceError);
    }

}
