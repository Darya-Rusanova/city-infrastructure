package ru.isu.cityinfra.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.isu.cityinfra.billing.enums.PaymentStatus;
import ru.isu.cityinfra.billing.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByTransactionRef(String transactionRef);
    
    Optional<Payment> findFirstByInvoiceIdAndStatus(Integer invoiceId, PaymentStatus status);
}