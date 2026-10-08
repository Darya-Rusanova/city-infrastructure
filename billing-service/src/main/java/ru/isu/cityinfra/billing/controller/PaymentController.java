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
import org.springframework.beans.factory.annotation.Value;

import ru.isu.cityinfra.billing.exception.UnauthorizedException;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${webhook.secret:}")
    private String webhookSecret;

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
            @RequestHeader(value = "X-Webhook-Secret", required = false) String secret,
            @Valid @RequestBody WebhookRequestDto request) {
        if (webhookSecret != null && !webhookSecret.isBlank()
                && !webhookSecret.equals(secret)) {
            throw new UnauthorizedException("Неверный секрет webhook");
        }
        return ResponseEntity.ok(paymentService.handleWebhook(request));
    }
}