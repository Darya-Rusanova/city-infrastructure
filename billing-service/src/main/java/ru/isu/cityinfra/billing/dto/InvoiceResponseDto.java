package ru.isu.cityinfra.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.isu.cityinfra.billing.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class InvoiceResponseDto {
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private String description;
    private InvoiceStatus status;
    private String nameStatus;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
}