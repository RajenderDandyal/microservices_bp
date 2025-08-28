package com.yourcompany.invoice.service.service;


import com.yourcompany.invoice.service.dto.InvoiceDTO;
import com.yourcompany.invoice.service.entities.Invoice;
import com.yourcompany.invoice.service.repos.InvoiceRepository;
import com.yourcompany.invoice.service.exception.ApiException;

// import com.yourcompany.utils.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public List<InvoiceDTO> getInvoiceList(String userId, HttpServletRequest request) {

        if (StringUtils.isEmpty(userId)) {
            log.info("userId is missing!!!");
             throw new ApiException(HttpStatus.BAD_REQUEST.value(), "Bad request",
             request.getPathInfo(),
             List.of("userId is missing in request."));
        }

        log.info("Calling Database with userId: {} ", userId);
        List<Invoice> invoices = invoiceRepository.findAllByUserId(userId);
        if (invoices.isEmpty()) {
            log.info("userId not found in db");
             throw new ApiException(HttpStatus.BAD_REQUEST.value(), "Bad request",
             request.getPathInfo(),
             List.of("userId not found in db."));
        }

        log.info("Returning invoice-service response");
        return invoices.stream()
                .map(this::populateInvoiceDTO)
                .toList();

    }

    private InvoiceDTO populateInvoiceDTO(Invoice invoice) {

        InvoiceDTO dto = new InvoiceDTO();

        dto.setInvoiceId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setProductIds(invoice.getProductIds());
        dto.setUpdatedTime(invoice.getUpdatedTime().toString());

        return dto;
    }
}
