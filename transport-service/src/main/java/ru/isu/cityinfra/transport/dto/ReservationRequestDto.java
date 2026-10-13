package ru.isu.cityinfra.transport.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDto {
    @NotNull(message = "Введите начальное время брони")
    @Future(message = "Время должно быть будущее")
    private LocalDateTime startTime;
    @NotNull(message = "Введите конечное время брони")
    @Future(message = "Время должно быть будущее")
    private LocalDateTime endTime;
}
