package ru.isu.cityinfra.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotDto {
    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer totalSpots;
    private Integer availableSpots;
}
