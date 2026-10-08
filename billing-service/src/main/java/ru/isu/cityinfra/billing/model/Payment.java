package ru.isu.cityinfra.billing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isu.cityinfra.billing.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_invoice", columnList = "invoice_id"),
        @Index(name = "idx_payments_status", columnList = "status"),
        @Index(name = "idx_payments_ref", columnList = "transaction_ref")
})
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "invoice_id", nullable = false)
    private Integer invoiceId;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;
    @Column(name = "transaction_ref", length = 100, unique = true)
    private String transactionRef;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}