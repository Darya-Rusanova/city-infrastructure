package ru.isu.cityinfra.billing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookRequestDto {
    @NotBlank(message = "Укажите transactionRef")
    private String transactionRef;
    @NotBlank(message = "Укажите статус")
    private String status;
}