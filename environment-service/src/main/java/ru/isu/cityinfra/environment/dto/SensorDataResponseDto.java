package ru.isu.cityinfra.environment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class SensorDataResponseDto {
    private Integer id;
    private Integer sensorId;
    private Double value;
    private String unit;
    private LocalDateTime recordedAt;
}