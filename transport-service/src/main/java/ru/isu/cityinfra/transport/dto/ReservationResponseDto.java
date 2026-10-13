package ru.isu.cityinfra.transport.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.isu.cityinfra.transport.enums.ReservationStatus;
import ru.isu.cityinfra.transport.model.ParkingLot;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ReservationResponseDto {
    private Integer id;
    private Integer parkingLotId;
    private Integer userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;
    private String statusName;
    private LocalDateTime createdAt;

}
