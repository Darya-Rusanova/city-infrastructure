package ru.isu.cityinfra.environment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.Size;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataRequestDto {
    @NotNull(message = "Укажите id датчика")
    private Integer sensorId;
    @NotNull(message = "Укажите значение")
    private Double value;
    @Size(max = 20, message = "Единица измерения не длиннее 20 символов")
    private String unit;
}