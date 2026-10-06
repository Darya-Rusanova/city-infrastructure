package ru.isu.cityinfra.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.cityinfra.billing.enums.InvoiceStatus;
import ru.isu.cityinfra.billing.model.Invoice;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByUserIdOrderByDueDateAsc(Integer userId);
    List<Invoice> findByUserIdAndStatusOrderByDueDateAsc(Integer userId, InvoiceStatus status);
}