package ru.isu.cityinfra.frontend.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Integer id;
    private Integer invoiceId;
    private BigDecimal amount;
    private String status;
    private String nameStatus;
    private String transactionRef;
}
