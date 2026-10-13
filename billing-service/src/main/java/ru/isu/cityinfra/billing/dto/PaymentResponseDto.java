package ru.isu.cityinfra.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.isu.cityinfra.billing.enums.PaymentStatus;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PaymentResponseDto {
    private Integer id;
    private Integer invoiceId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String nameStatus;
    private String transactionRef;
}