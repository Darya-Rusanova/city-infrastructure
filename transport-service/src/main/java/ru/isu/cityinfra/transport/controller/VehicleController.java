package ru.isu.cityinfra.transport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.isu.cityinfra.transport.dto.VehicleResponseDto;
import ru.isu.cityinfra.transport.enums.VehicleStatus;
import ru.isu.cityinfra.transport.enums.VehicleType;
import ru.isu.cityinfra.transport.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;
    @GetMapping
    public ResponseEntity<List<VehicleResponseDto>> getAllVehicle(@RequestParam(required = false)VehicleStatus status,
                                                            @RequestParam(required = false)VehicleType type,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String dir){
        List<VehicleResponseDto> response = vehicleService.getAllVehicles(type, status, sortBy, dir);
        return ResponseEntity.ok(response);
    }
}
