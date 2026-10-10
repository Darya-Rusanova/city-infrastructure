package ru.isu.cityinfra.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.isu.cityinfra.billing.enums.PaymentStatus;
import ru.isu.cityinfra.billing.model.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByTransactionRef(String transactionRef);
    
    boolean existsByInvoiceIdAndStatus(Integer invoiceId, PaymentStatus status);
}