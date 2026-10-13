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
public class ReservationDto {
    private Integer id;
    private Integer parkingLotId;
    private Integer userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String statusName;
    private LocalDateTime createdAt;
}
