package ru.isu.cityinfra.billing.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.cityinfra.billing.dto.InvoiceResponseDto;
import ru.isu.cityinfra.billing.enums.InvoiceStatus;
import ru.isu.cityinfra.billing.exception.ForbiddenException;
import ru.isu.cityinfra.billing.exception.NotFoundException;
import ru.isu.cityinfra.billing.model.Invoice;
import ru.isu.cityinfra.billing.repository.InvoiceRepository;

import java.util.List;

@Service
@Slf4j
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<InvoiceResponseDto> getInvoices(Integer requestedUserId,
                                                Integer currentUserId,
                                                boolean isAdmin,
                                                InvoiceStatus status) {
        if (!isAdmin && !requestedUserId.equals(currentUserId)) {
            throw new ForbiddenException("Нет доступа к счетам другого пользователя");
        }
        log.info("Запрос счетов userId={}, status={}, isAdmin={}", requestedUserId, status, isAdmin);
        List<Invoice> invoices = status == null
                ? invoiceRepository.findByUserIdOrderByDueDateAsc(requestedUserId)
                : invoiceRepository.findByUserIdAndStatusOrderByDueDateAsc(requestedUserId, status);
        return invoices.stream().map(this::toDto).toList();
    }

    public Invoice getById(Integer id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Счет с id=" + id + " не найден"));
    }

    private InvoiceResponseDto toDto(Invoice invoice) {
        return InvoiceResponseDto.builder()
                .id(invoice.getId())
                .userId(invoice.getUserId())
                .amount(invoice.getAmount())
                .description(invoice.getDescription())
                .status(invoice.getStatus())
                .nameStatus(invoice.getStatus().getDisplayName())
                .dueDate(invoice.getDueDate())
                .createdAt(invoice.getCreatedAt())
                .build();
    }
}