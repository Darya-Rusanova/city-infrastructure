package ru.isu.cityinfra.environment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data", indexes = {
        @Index(name = "idx_sensor_data_sensor", columnList = "sensor_id"),
        @Index(name = "idx_sensor_data_recorded", columnList = "recorded_at")
})
@Getter
@Setter
@NoArgsConstructor
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private double value;
    @Column(length = 20)
    private String unit;
    @Column(name = "recorded_at")
    private LocalDateTime recordedAt = LocalDateTime.now();
    @ManyToOne()
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;
}