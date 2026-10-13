package ru.isu.cityinfra.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.cityinfra.transport.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
}
