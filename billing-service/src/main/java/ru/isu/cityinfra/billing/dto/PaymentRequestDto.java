package ru.isu.cityinfra.billing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    @NotNull(message = "Укажите id счета")
    private Integer invoiceId;
    @NotNull(message = "Укажите сумму")
    private BigDecimal amount;
}