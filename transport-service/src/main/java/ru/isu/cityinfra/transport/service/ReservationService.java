package ru.isu.cityinfra.transport.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.cityinfra.transport.dto.ParkingResponseDto;
import ru.isu.cityinfra.transport.dto.ReservationRequestDto;
import ru.isu.cityinfra.transport.dto.ReservationResponseDto;
import ru.isu.cityinfra.transport.enums.ReservationStatus;
import ru.isu.cityinfra.transport.exception.ConflictException;
import ru.isu.cityinfra.transport.exception.NotFoundException;
import ru.isu.cityinfra.transport.model.ParkingLot;
import ru.isu.cityinfra.transport.model.Reservation;
import ru.isu.cityinfra.transport.repository.ParkingRepository;
import ru.isu.cityinfra.transport.repository.ReservationRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ReservationService {
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Transactional
    public ReservationResponseDto reserveLot(Integer userId, Integer parkingLotId, ReservationRequestDto request){
        LocalDateTime start = request.getStartTime();
        LocalDateTime end = request.getEndTime();
        log.info("Попытка бронирования слота {}-{}",start,end);
        if (start.isAfter(end)){
            log.error("Ошибка: время начала позже времени конца {}-{}",start,end);
            throw new ConflictException("Время начала должно быть раньше конца");
        }
        ParkingLot parkingLot = parkingRepository.findById(parkingLotId).orElse(null);
        if (parkingLot==null){
            log.error("Ошибка: парковки с id={} не существует",parkingLotId);
            throw new NotFoundException("Парковки c id="+parkingLotId+" не существует");
        }
        if(parkingLot.getAvailableSpots() == 0) {
            log.error("Ошибка с бронированием: нет свободных мест");
            throw new ConflictException("На парковке нет свободных мест");
        }
        Reservation reservation = Reservation.builder()
                .userId(userId)
                .startTime(start)
                .endTime(end)
                .status(ReservationStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .parkingLotId(parkingLotId)
                .build();
        parkingLot.setAvailableSpots(parkingLot.getAvailableSpots()-1);
        Reservation saved = reservationRepository.save(reservation);
        log.info("Успешное бронирование слота {}-{}, количество свободных слотов на парковке с id={} равно {}",start,end,parkingLotId,parkingLot.getAvailableSpots());
        return toDto(saved);
    }

    private ReservationResponseDto toDto(Reservation saved){
        ReservationResponseDto response = ReservationResponseDto.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .status(saved.getStatus())
                .statusName(saved.getStatus().getDisplayName())
                .createdAt(saved.getCreatedAt())
                .parkingLotId(saved.getParkingLotId())
                .build();
        return response;
    }
}
