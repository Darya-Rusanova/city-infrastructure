package ru.isu.cityinfra.environment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.cityinfra.environment.model.SensorData;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {
    List<SensorData> findBySensorIdOrderByRecordedAtDesc(Integer sensorId, Pageable pageable);
    List<SensorData> findBySensorIdAndRecordedAtBetweenOrderByRecordedAtDesc(
            Integer sensorId, LocalDateTime from, LocalDateTime to);
}