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
public class VehicleDto {
    private Integer id;
    private String status;
    private String statusName;
    private String type;
    private String typeName;
    private Double latitude;
    private Double longitude;
    private LocalDateTime updatedAt;
}
