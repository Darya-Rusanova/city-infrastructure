package ru.isu.cityinfra.environment.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isu.cityinfra.environment.dto.SensorDataRequestDto;
import ru.isu.cityinfra.environment.dto.SensorDataResponseDto;
import ru.isu.cityinfra.environment.dto.SensorResponseDto;
import ru.isu.cityinfra.environment.enums.SensorStatus;
import ru.isu.cityinfra.environment.enums.SensorType;
import ru.isu.cityinfra.environment.service.SensorService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @GetMapping
    public ResponseEntity<List<SensorResponseDto>> getSensors(
            @RequestParam(required = false) SensorType type,
            @RequestParam(required = false) SensorStatus status) {
        return ResponseEntity.ok(sensorService.getSensors(type, status));
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<List<SensorDataResponseDto>> getSensorData(
            @PathVariable Integer id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(sensorService.getSensorData(id, from, to));
    }

    @PostMapping("/data")
    public ResponseEntity<SensorDataResponseDto> addData(
            @Valid @RequestBody SensorDataRequestDto request) {
        return ResponseEntity.status(201).body(sensorService.addData(request));
    }
}