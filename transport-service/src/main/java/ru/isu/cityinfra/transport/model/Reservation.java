package ru.isu.cityinfra.transport.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isu.cityinfra.transport.enums.ReservationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_reservations_user", columnList = "user_id"),
        @Index(name = "idx_reservations_status", columnList = "status")
})
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
    private ReservationStatus type= ReservationStatus.ACTIVE;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ManyToOne()
    @JoinColumn(name = "parking_lot_id",nullable = false)
    private ParkingLot parkingLot;
}
