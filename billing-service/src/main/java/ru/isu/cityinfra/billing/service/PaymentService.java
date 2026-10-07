package ru.isu.cityinfra.billing.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isu.cityinfra.billing.client.NotificationClient;
import ru.isu.cityinfra.billing.dto.PaymentRequestDto;
import ru.isu.cityinfra.billing.dto.PaymentResponseDto;
import ru.isu.cityinfra.billing.dto.WebhookRequestDto;
import ru.isu.cityinfra.billing.enums.InvoiceStatus;
import ru.isu.cityinfra.billing.enums.PaymentStatus;
import ru.isu.cityinfra.billing.exception.ConflictException;
import ru.isu.cityinfra.billing.exception.ForbiddenException;
import ru.isu.cityinfra.billing.exception.NotFoundException;
import ru.isu.cityinfra.billing.model.Invoice;
import ru.isu.cityinfra.billing.model.Payment;
import ru.isu.cityinfra.billing.repository.InvoiceRepository;
import ru.isu.cityinfra.billing.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private NotificationClient notificationClient;

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto request,
                                            Integer currentUserId,
                                            boolean isAdmin) {
        log.info("Попытка оплаты счета id={} пользователем id={}", request.getInvoiceId(), currentUserId);
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new NotFoundException(
                        "Счет с id=" + request.getInvoiceId() + " не найден"));

        if (!isAdmin && !invoice.getUserId().equals(currentUserId)) {
            throw new ForbiddenException("Нельзя оплатить чужой счет");
        }
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new ConflictException("Счет уже оплачен");
        }
        if (invoice.getAmount().compareTo(request.getAmount()) != 0) {
            throw new ConflictException("Сумма платежа не совпадает с суммой счета");
        }

        Payment payment = new Payment();
        payment.setInvoiceId(invoice.getId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionRef("txn_" + UUID.randomUUID());
        payment.setCreatedAt(LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);
        log.info("Создан платеж id={} для счета id={}, transactionRef={}",
                saved.getId(), invoice.getId(), saved.getTransactionRef());
        return toDto(saved);
    }

    @Transactional
    public PaymentResponseDto handleWebhook(WebhookRequestDto request) {
        log.info("Получен webhook: transactionRef={}, status={}",
                request.getTransactionRef(), request.getStatus());
        Payment payment = paymentRepository.findByTransactionRef(request.getTransactionRef())
                .orElseThrow(() -> new NotFoundException(
                        "Платеж с transactionRef=" + request.getTransactionRef() + " не найден"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            log.warn("Повторный webhook для уже успешного платежа id={}", payment.getId());
            return toDto(payment);
        }
        if (payment.getStatus() == PaymentStatus.FAILED) {
            throw new ConflictException("Платеж уже завершился с ошибкой");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        Invoice invoice = invoiceRepository.findById(payment.getInvoiceId())
                .orElseThrow(() -> new NotFoundException(
                        "Счет с id=" + payment.getInvoiceId() + " не найден"));
        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);
        log.info("Платеж id={} успешно проведен, счет id={} оплачен",
                payment.getId(), invoice.getId());

        notificationClient.send(invoice.getUserId(),
                "Счет оплачен",
                "Счет №" + invoice.getId() + " успешно оплачен");

        return toDto(payment);
    }

    private PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto.builder()
                .id(payment.getId())
                .invoiceId(payment.getInvoiceId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .nameStatus(payment.getStatus().getDisplayName())
                .transactionRef(payment.getTransactionRef())
                .build();
    }
}