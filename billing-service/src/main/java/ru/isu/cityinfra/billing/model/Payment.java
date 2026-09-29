package ru.isu.cityinfra.billing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isu.cityinfra.transport.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_invoice", columnList = "invoice_id"),
        @Index(name = "idx_payments_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;
    @Column(name = "transaction_ref", length = 100)
    private String transactionRef;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne()
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
}