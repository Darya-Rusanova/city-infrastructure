package ru.isu.cityinfra.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ParkingResponseDto {
    private Integer id;
    private String name;
    private double latitude;
    private double longitude;
    private int totalSpots;
    private int availableSpots;
}