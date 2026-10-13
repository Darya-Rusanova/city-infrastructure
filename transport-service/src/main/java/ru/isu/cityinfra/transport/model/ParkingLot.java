package ru.isu.cityinfra.transport.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parking_lots")
@Getter
@Setter
@NoArgsConstructor
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    @Column(name = "total_spots",nullable = false)
    private int totalSpots;
    @Column(name="available_spots",nullable = false)
    private int availableSpots;
}
