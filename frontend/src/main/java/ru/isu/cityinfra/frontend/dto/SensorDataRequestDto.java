package ru.isu.cityinfra.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataRequestDto {
    private Integer sensorId;
    private Double value;
    private String unit;
}
