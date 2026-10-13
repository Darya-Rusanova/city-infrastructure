package ru.isu.cityinfra.billing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.billing.enums.InvoiceStatus;
import ru.isu.cityinfra.billing.model.Invoice;
import ru.isu.cityinfra.billing.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public void run(String... args) {
        if (invoiceRepository.count() > 0) {
            log.info("Invoices already initialized: {} records", invoiceRepository.count());
            return;
        }
        log.info("Initializing invoices...");

        invoiceRepository.save(createInvoice(2, "1500.00", "Оплата ЖКХ за сентябрь",
                InvoiceStatus.UNPAID, LocalDate.now().plusDays(10)));
        invoiceRepository.save(createInvoice(2, "700.00", "Оплата ЖКХ за август",
                InvoiceStatus.PAID, LocalDate.now().minusDays(20)));
        invoiceRepository.save(createInvoice(3, "2500.00", "Оплата ЖКХ за сентябрь",
                InvoiceStatus.OVERDUE, LocalDate.now().minusDays(5)));

        log.info("Invoices initialized: {} records", invoiceRepository.count());
    }

    private Invoice createInvoice(Integer userId, String amount, String description,
                                  InvoiceStatus status, LocalDate dueDate) {
        Invoice i = new Invoice();
        i.setUserId(userId);
        i.setAmount(new BigDecimal(amount));
        i.setDescription(description);
        i.setStatus(status);
        i.setDueDate(dueDate);
        i.setCreatedAt(LocalDateTime.now());
        return i;
    }
}