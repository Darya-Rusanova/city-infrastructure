package ru.isu.cityinfra.transport.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.isu.cityinfra.transport.dto.ParkingResponseDto;
import ru.isu.cityinfra.transport.dto.ReservationRequestDto;
import ru.isu.cityinfra.transport.dto.ReservationResponseDto;
import ru.isu.cityinfra.transport.service.ParkingService;
import ru.isu.cityinfra.transport.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/parking")
public class ParkingController {
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ReservationService reservationService;
    @GetMapping
    public ResponseEntity<List<ParkingResponseDto>> getAllParkingLots(@RequestParam(required = false) boolean available,
                                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                                      @RequestParam(defaultValue = "asc")String dir){
        List<ParkingResponseDto> response = parkingService.getAllParkingLot(available,sortBy,dir);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ReservationResponseDto> reserveParkingLot(@PathVariable Integer id,
                                                                    @Valid @RequestBody ReservationRequestDto request,
                                                                    Authentication authentication){
        Integer userId = Integer.parseInt(authentication.getName());
        ReservationResponseDto response = reservationService.reserveLot(userId,id,request);
        return ResponseEntity.status(201).body(response);
    }
}
