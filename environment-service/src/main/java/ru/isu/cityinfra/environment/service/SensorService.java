package ru.isu.cityinfra.environment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isu.cityinfra.environment.dto.SensorDataRequestDto;
import ru.isu.cityinfra.environment.dto.SensorDataResponseDto;
import ru.isu.cityinfra.environment.dto.SensorResponseDto;
import ru.isu.cityinfra.environment.enums.SensorStatus;
import ru.isu.cityinfra.environment.enums.SensorType;
import ru.isu.cityinfra.environment.exception.ConflictException;
import ru.isu.cityinfra.environment.exception.NotFoundException;
import ru.isu.cityinfra.environment.model.Sensor;
import ru.isu.cityinfra.environment.model.SensorData;
import ru.isu.cityinfra.environment.repository.SensorDataRepository;
import ru.isu.cityinfra.environment.repository.SensorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SensorService {

    private static final int DEFAULT_LIMIT = 100;

    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private SensorDataRepository sensorDataRepository;

    public List<SensorResponseDto> getSensors(SensorType type, SensorStatus status) {
        log.info("Запрос датчиков: type={}, status={}", type, status);
        List<Sensor> sensors;
        if (type != null && status != null) {
            sensors = sensorRepository.findByTypeAndStatus(type, status);
        } else if (type != null) {
            sensors = sensorRepository.findByType(type);
        } else if (status != null) {
            sensors = sensorRepository.findByStatus(status);
        } else {
            sensors = sensorRepository.findAll();
        }
        return sensors.stream().map(this::toDto).toList();
    }

    public List<SensorDataResponseDto> getSensorData(Integer sensorId,
                                                     LocalDateTime from,
                                                     LocalDateTime to) {
        sensorRepository.findById(sensorId)
                .orElseThrow(() -> new NotFoundException("Датчик с id=" + sensorId + " не найден"));

        if ((from == null) != (to == null)) {
            throw new ConflictException("Укажите обе границы интервала или ни одной");
        }
        if (from != null && from.isAfter(to)) {
            throw new ConflictException("Начало интервала позже конца");
        }
        List<SensorData> data;
        if (from != null && to != null) {
            data = sensorDataRepository
                    .findBySensorIdAndRecordedAtBetweenOrderByRecordedAtDesc(sensorId, from, to);
        } else {
            data = sensorDataRepository
                    .findBySensorIdOrderByRecordedAtDesc(sensorId, PageRequest.of(0, DEFAULT_LIMIT));
        }
        log.info("Данные датчика id={}: {} записей", sensorId, data.size());
        return data.stream().map(this::toDataDto).toList();
    }

    @Transactional
    public SensorDataResponseDto addData(SensorDataRequestDto request) {
        log.info("Запись показания: sensorId={}, value={}",
                request.getSensorId(), request.getValue());
        Sensor sensor = sensorRepository.findById(request.getSensorId())
                .orElseThrow(() -> new NotFoundException(
                        "Датчик с id=" + request.getSensorId() + " не найден"));
        if (sensor.getStatus() != SensorStatus.ACTIVE) {
            throw new ConflictException("Датчик неактивен или на обслуживании, запись невозможна");
        }
        SensorData data = new SensorData();
        data.setSensorId(sensor.getId());
        data.setValue(request.getValue());
        data.setUnit(request.getUnit());
        data.setRecordedAt(LocalDateTime.now());
        SensorData saved = sensorDataRepository.save(data);
        log.info("Сохранено показание id={} датчика id={}", saved.getId(), sensor.getId());
        return toDataDto(saved);
    }

    private SensorResponseDto toDto(Sensor sensor) {
        return SensorResponseDto.builder()
                .id(sensor.getId())
                .name(sensor.getName())
                .type(sensor.getType())
                .nameType(sensor.getType().getDisplayName())
                .latitude(sensor.getLatitude())
                .longitude(sensor.getLongitude())
                .status(sensor.getStatus())
                .nameStatus(sensor.getStatus().getDisplayName())
                .build();
    }

    private SensorDataResponseDto toDataDto(SensorData data) {
        return SensorDataResponseDto.builder()
                .id(data.getId())
                .sensorId(data.getSensorId())
                .value(data.getValue())
                .unit(data.getUnit())
                .recordedAt(data.getRecordedAt())
                .build();
    }
}