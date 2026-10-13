package ru.isu.cityinfra.environment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.cityinfra.environment.enums.SensorStatus;
import ru.isu.cityinfra.environment.enums.SensorType;
import ru.isu.cityinfra.environment.model.Sensor;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    List<Sensor> findByType(SensorType type);
    List<Sensor> findByStatus(SensorStatus status);
    List<Sensor> findByTypeAndStatus(SensorType type, SensorStatus status);
}