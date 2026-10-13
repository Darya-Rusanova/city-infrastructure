package ru.isu.cityinfra.environment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.isu.cityinfra.environment.enums.SensorStatus;
import ru.isu.cityinfra.environment.enums.SensorType;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class SensorResponseDto {
    private Integer id;
    private String name;
    private SensorType type;
    private String nameType;
    private double latitude;
    private double longitude;
    private SensorStatus status;
    private String nameStatus;
}