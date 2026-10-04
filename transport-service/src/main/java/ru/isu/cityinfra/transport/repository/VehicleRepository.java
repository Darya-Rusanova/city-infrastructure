package ru.isu.cityinfra.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.isu.cityinfra.transport.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>,
        JpaSpecificationExecutor<Vehicle> {
}