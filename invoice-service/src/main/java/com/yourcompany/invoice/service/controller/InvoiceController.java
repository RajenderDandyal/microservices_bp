package com.yourcompany.invoice.service.controller;


import com.yourcompany.invoice.service.dto.InvoiceDTO;
import com.yourcompany.invoice.service.service.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/invoices")
    public List<InvoiceDTO> getInvoices(@RequestParam("user_id") String userId, HttpServletRequest request) {
        log.info("Received request for userId : {} ", userId);

        return invoiceService.getInvoiceList(userId, request);
    }

}
