package ru.isu.cityinfra.transport.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isu.cityinfra.transport.enums.VehicleStatus;
import ru.isu.cityinfra.transport.enums.VehicleType;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status = VehicleStatus.ACTIVE;
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private  double longitude;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
