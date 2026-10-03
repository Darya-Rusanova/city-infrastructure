package ru.isu.cityinfra.transport.model;

import jakarta.persistence.*;
import lombok.*;
import ru.isu.cityinfra.transport.enums.ReservationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_reservations_user", columnList = "user_id"),
        @Index(name = "idx_reservations_status", columnList = "status")
})
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id",nullable = false)
    private Integer userId;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time",nullable = false)
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status= ReservationStatus.ACTIVE;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "parking_lot_id",nullable = false)
    private Integer parkingLotId;
}
