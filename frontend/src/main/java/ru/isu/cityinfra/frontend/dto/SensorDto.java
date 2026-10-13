package ru.isu.cityinfra.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDto {
    private Integer id;
    private String name;
    private String type;
    private String nameType;
    private Double latitude;
    private Double longitude;
    private String status;
    private String nameStatus;
}
