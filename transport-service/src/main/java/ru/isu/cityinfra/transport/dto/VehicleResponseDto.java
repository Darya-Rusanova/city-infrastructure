package ru.isu.cityinfra.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.isu.cityinfra.transport.enums.VehicleStatus;
import ru.isu.cityinfra.transport.enums.VehicleType;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class VehicleResponseDto {
    private Integer id;
    private VehicleStatus status;
    private String statusName;
    private VehicleType type;
    private String typeName;
    private double latitude;
    private double longitude;
    private LocalDateTime updatedAt;
}