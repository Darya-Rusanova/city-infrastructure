package ru.isu.cityinfra.billing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isu.cityinfra.billing.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices", indexes = {
        @Index(name = "idx_invoices_user", columnList = "user_id"),
        @Index(name = "idx_invoices_status", columnList = "status"),
        @Index(name = "idx_invoices_due_date", columnList = "due_date")
})
@Getter
@Setter
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    @Column(length = 200)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceStatus status = InvoiceStatus.UNPAID;
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}