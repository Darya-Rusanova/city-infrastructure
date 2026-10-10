package ru.isu.cityinfra.frontend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private String description;
    private String status;
    private String nameStatus;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
}
