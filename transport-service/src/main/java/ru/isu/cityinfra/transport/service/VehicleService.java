package ru.isu.cityinfra.transport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.isu.cityinfra.transport.dto.VehicleResponseDto;
import ru.isu.cityinfra.transport.enums.VehicleStatus;
import ru.isu.cityinfra.transport.enums.VehicleType;
import ru.isu.cityinfra.transport.exception.BadRequestException;
import ru.isu.cityinfra.transport.model.Vehicle;
import ru.isu.cityinfra.transport.repository.VehicleRepository;

import java.util.List;

@Service
@Slf4j
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    private static final List<String> SORT_FIELDS =
            List.of("id", "type", "status", "latitude", "longitude", "updatedAt");

    private Sort buildSort(String sortBy, String dir) {
        if (!SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException(
                    "Недопустимое поле сортировки: " + sortBy + ". Допустимые: " + SORT_FIELDS);
        }
        if (!"asc".equalsIgnoreCase(dir) && !"desc".equalsIgnoreCase(dir)) {
            throw new BadRequestException("Направление сортировки должно быть asc или desc");
        }
        return Sort.by(Sort.Direction.fromString(dir), sortBy);
    }

    public List<VehicleResponseDto> getAllVehicles(VehicleType type, VehicleStatus status, String sortBy, String dir){
        log.info("Вывод всех транспортных средств с параметрами: тип - {}, статус - {}, поле сортировки - {}, направление сортировки - {}",type,status,sortBy,dir);
        Specification<Vehicle> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        if (type!=null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"),type));
        }
        if (status!=null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"),status));
        }
        Sort sort = buildSort(sortBy,dir);
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
