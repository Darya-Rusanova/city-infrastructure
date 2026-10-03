package ru.isu.cityinfra.transport.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.isu.cityinfra.transport.dto.ParkingResponseDto;
import ru.isu.cityinfra.transport.model.ParkingLot;
import ru.isu.cityinfra.transport.repository.ParkingRepository;

import java.util.List;

@Service
public class ParkingService {
    @Autowired
    private ParkingRepository parkingRepository;

    public List<ParkingResponseDto> getAllParkingLot(boolean available, String sortBy, String dir){
        Sort sort = dir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        List<ParkingLot> parkingLots;
        if(available){
            parkingLots = parkingRepository.findByAvailableSpotsGreaterThan(0,sort);
        }
        else{
            parkingLots = parkingRepository.findAll(sort);
        }
        return parkingLots.stream().map(this::toDto).toList();
    }

    private ParkingResponseDto toDto(ParkingLot parkingLot){
        ParkingResponseDto response = ParkingResponseDto.builder()
                .id(parkingLot.getId())
                .name(parkingLot.getName())
                .latitude(parkingLot.getLatitude())
                .longitude(parkingLot.getLongitude())
                .totalSpots(parkingLot.getTotalSpots())
                .availableSpots(parkingLot.getAvailableSpots())
                .build();
        return response;
    }

}
