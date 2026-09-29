package ru.isu.cityinfra.environment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isu.cityinfra.transport.enums.SensorStatus;
import ru.isu.cityinfra.transport.enums.SensorType;

@Entity
@Table(name = "sensors", indexes = {
        @Index(name = "idx_sensors_type", columnList = "type"),
        @Index(name = "idx_sensors_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 100)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorType type;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorStatus status = SensorStatus.ACTIVE;
}