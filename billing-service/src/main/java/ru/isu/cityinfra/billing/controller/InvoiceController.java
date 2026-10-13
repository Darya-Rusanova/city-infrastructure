package ru.isu.cityinfra.billing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.isu.cityinfra.billing.dto.InvoiceResponseDto;
import ru.isu.cityinfra.billing.enums.InvoiceStatus;
import ru.isu.cityinfra.billing.service.InvoiceService;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{userId}/invoices")
    public ResponseEntity<List<InvoiceResponseDto>> getInvoices(
            @PathVariable Integer userId,
            @RequestParam(required = false) InvoiceStatus status,
            Authentication authentication) {
        Integer currentUserId = Integer.parseInt(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(invoiceService.getInvoices(userId, currentUserId, isAdmin, status));
    }
}