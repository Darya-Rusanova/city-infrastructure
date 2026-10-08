package ru.isu.cityinfra.transport.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.isu.cityinfra.transport.model.ParkingLot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingRepository extends JpaRepository<ParkingLot,Integer>,
        JpaSpecificationExecutor<ParkingLot> {
    List<ParkingLot> findByAvailableSpotsGreaterThan(Integer minVal, Sort sort);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ParkingLot p WHERE p.id = :id")
    Optional<ParkingLot> findByIdForUpdate(@Param("id") Integer id);
}
