package ru.isu.cityinfra.frontend.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.frontend.dto.InvoiceDto;
import ru.isu.cityinfra.frontend.dto.PaymentDto;
import ru.isu.cityinfra.frontend.dto.PaymentRequestDto;
import ru.isu.cityinfra.frontend.dto.WebhookRequestDto;

@Component
public class BillingClient {

    private final ApiGateway api;
    private final String baseUrl;
    private final String webhookSecret;

    public BillingClient(ApiGateway api,
                         @Value("${services.billing.url}") String baseUrl,
                         @Value("${services.billing.webhook-secret:}") String webhookSecret) {
        this.api = api;
        this.baseUrl = baseUrl;
        this.webhookSecret = webhookSecret;
    }

    public List<InvoiceDto> getInvoices(String token, Integer userId, String status) {
        return api.getList(ApiGateway.uri(baseUrl, "/accounts/" + userId + "/invoices", "status", status),
                token, InvoiceDto[].class);
    }

    public PaymentDto createPayment(String token, PaymentRequestDto request) {
        return api.post(ApiGateway.uri(baseUrl, "/payments"), token, request, PaymentDto.class);
    }

    /**
     * Имитация платежного провайдера: сообщает биллингу, что платеж прошел успешно.
     * Если задан секрет (WEBHOOK_SECRET), он передается в заголовке X-Webhook-Secret.
     */
    public PaymentDto confirmPayment(String transactionRef) {
        Map<String, String> headers = new HashMap<>();
        if (webhookSecret != null && !webhookSecret.isBlank()) {
            headers.put("X-Webhook-Secret", webhookSecret);
        }
        return api.post(ApiGateway.uri(baseUrl, "/payments/webhook/success"), null,
                new WebhookRequestDto(transactionRef, "SUCCESS"), headers, PaymentDto.class);
    }
}
