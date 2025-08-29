package com.yourcompany.user.service.service;


import com.yourcompany.user.service.client.InvoiceServiceFeignClient;
import com.yourcompany.user.service.dto.InvoiceDTO;
import com.yourcompany.user.service.exception.ApiException;
import com.yourcompany.user.service.exception.InvoiceException;
import com.yourcompany.user.service.exception.UserException;
import feign.FeignException;
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


//    @Retryable(retryFor = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000)) // This is one  of doing retry, another way is using RetryTemplate, see RetryConfig class
    public List<InvoiceDTO> callInvoiceServiceAndGetInvoiceDTOList(String userId, HttpServletRequest request) {

        AtomicReference<List<InvoiceDTO>> invoiceResponse = new AtomicReference<>(new ArrayList<>());

        try {

            retryTemplate.execute(arg -> {
                invoiceResponse.set(invoiceServiceFeignClient.getInvoices(userId));
                return invoiceResponse;
            });

        } catch (FeignException.NotFound invoiceException) {
            throw new ApiException(HttpStatus.NOT_FOUND.value(), "Not Found request to invoice service", request.getPathInfo(), List.of("invoice-service is down"));
        } catch (FeignException.BadRequest badRequestException) {
            throw new ApiException(HttpStatus.BAD_REQUEST.value(), "Bad request to invoice service", request.getPathInfo(), List.of(String.format("invoice not found in db with userId: %s", userId)));
        }catch (FeignException.Forbidden forbiddenException) {
            throw new ApiException(HttpStatus.FORBIDDEN.value(), "Access denied", request.getPathInfo(), List.of("You do not have permission to access this resource"));
        } catch (FeignException.InternalServerError internalServerErrorException) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Downstream service error", request.getPathInfo(), List.of("Invoice service encountered an error"));
        } catch (ServiceUnavailable ex) {
            throw new ApiException(HttpStatus.NOT_FOUND.value(), "Downstream service unavailable", request.getPathInfo(),  List.of("invoice-service is down"));
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), request.getPathInfo(),  List.of("Some issue with server"));
        }

        return invoiceResponse.get();
    }


}
