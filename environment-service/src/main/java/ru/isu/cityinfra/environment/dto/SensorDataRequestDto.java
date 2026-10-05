package ru.isu.cityinfra.environment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private String unit;
}