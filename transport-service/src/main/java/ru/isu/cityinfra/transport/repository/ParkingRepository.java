package ru.isu.cityinfra.transport.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.isu.cityinfra.transport.model.ParkingLot;

import java.util.List;

public interface ParkingRepository extends JpaRepository<ParkingLot,Integer>,
        JpaSpecificationExecutor<ParkingLot> {
    List<ParkingLot> findByAvailableSpotsGreaterThan(Integer minVal, Sort sort);
}
