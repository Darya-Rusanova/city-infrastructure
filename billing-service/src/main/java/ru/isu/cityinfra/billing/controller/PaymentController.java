package ru.isu.cityinfra.billing.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.isu.cityinfra.billing.dto.PaymentRequestDto;
import ru.isu.cityinfra.billing.dto.PaymentResponseDto;
import ru.isu.cityinfra.billing.dto.WebhookRequestDto;
import ru.isu.cityinfra.billing.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(
            @Valid @RequestBody PaymentRequestDto request,
            Authentication authentication) {
        Integer currentUserId = Integer.parseInt(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.status(201).body(
                paymentService.createPayment(request, currentUserId, isAdmin));
    }

    @PostMapping("/webhook/success")
    public ResponseEntity<PaymentResponseDto> webhookSuccess(
            @Valid @RequestBody WebhookRequestDto request) {
        return ResponseEntity.ok(paymentService.handleWebhook(request));
    }
}