package com.yourcompany.invoice.service.repos;

import com.yourcompany.invoice.service.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    List<Invoice> findAllByUserId(String id);

}
