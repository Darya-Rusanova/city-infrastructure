package ru.isu.cityinfra.transport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.isu.cityinfra.transport.dto.VehicleResponseDto;
import ru.isu.cityinfra.transport.enums.VehicleStatus;
import ru.isu.cityinfra.transport.enums.VehicleType;
import ru.isu.cityinfra.transport.model.Vehicle;
import ru.isu.cityinfra.transport.repository.VehicleRepository;

import java.util.List;

@Service
@Slf4j
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<VehicleResponseDto> getAllVehicles(VehicleType type, VehicleStatus status, String sortBy, String dir){
        log.info("Вывод всех транспортных средств с параметрами: тип - {}, статус - {}, поле сортировки - {}, направление сортировки - {}",type,status,sortBy,dir);
        Specification<Vehicle> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        if (type!=null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"),type));
        }
        if (status!=null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),status));
        }
        Sort sort = dir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        List<Vehicle> vehicles = vehicleRepository.findAll(spec,sort);
        return vehicles.stream().map(this::toDto).toList();
    }
    private VehicleResponseDto toDto(Vehicle vehicle){
        VehicleResponseDto response = VehicleResponseDto.builder()
                .id(vehicle.getId())
                .status(vehicle.getStatus())
                .statusName(vehicle.getStatus().getDisplayName())
                .type(vehicle.getType())
                .typeName(vehicle.getType().getDisplayName())
                .latitude(vehicle.getLatitude())
                .longitude(vehicle.getLongitude())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
        return response;
    }
}
