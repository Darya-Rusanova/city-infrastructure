package ru.isu.cityinfra.billing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.Positive;

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
    @Positive(message = "Сумма должна быть положительной")
    private BigDecimal amount;
}