package ru.isu.cityinfra.frontend.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataDto {
    private Integer id;
    private Integer sensorId;
    private Double value;
    private String unit;
    private LocalDateTime recordedAt;
}
