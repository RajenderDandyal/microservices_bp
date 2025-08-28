package com.yourcompany.user.service.service;


import com.yourcompany.user.service.client.InvoiceServiceFeignClient;
import com.yourcompany.user.service.dto.InvoiceDTO;
import com.yourcompany.user.service.exception.ApiException;
import com.yourcompany.user.service.exception.InvoiceException;
import com.yourcompany.user.service.exception.UserException;
import feign.FeignException.ServiceUnavailable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceService {

    private final InvoiceServiceFeignClient invoiceServiceFeignClient;

    private final RetryTemplate retryTemplate;


    @Retryable(retryFor = {Exception.class}, maxAttempts = 4, backoff = @Backoff(delay = 3000))
    public List<InvoiceDTO> callInvoiceServiceAndGetInvoiceDTOList(String userId, HttpServletRequest request) {

        AtomicReference<List<InvoiceDTO>> invoiceResponse = new AtomicReference<>(new ArrayList<>());

        try {

            retryTemplate.execute(arg -> {
                invoiceResponse.set(invoiceServiceFeignClient.getInvoices(userId));
                return invoiceResponse;
            });

        } catch (ApiException invoiceException) {
            throw invoiceException;
        } catch (ServiceUnavailable ex) {
            throw new ApiException(HttpStatus.NOT_FOUND.value(), "Downstream service unavailable", request.getPathInfo(),  List.of("invoice-service is down"));
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), request.getPathInfo(),  List.of("Some issue with server"));
        }

        return invoiceResponse.get();
    }


}
