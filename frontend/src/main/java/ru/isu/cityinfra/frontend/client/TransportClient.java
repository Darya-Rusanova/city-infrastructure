package ru.isu.cityinfra.frontend.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.frontend.dto.ParkingLotDto;
import ru.isu.cityinfra.frontend.dto.ReservationDto;
import ru.isu.cityinfra.frontend.dto.ReservationRequestDto;
import ru.isu.cityinfra.frontend.dto.VehicleDto;

@Component
public class TransportClient {

    private final ApiGateway api;
    private final String baseUrl;

    public TransportClient(ApiGateway api, @Value("${services.transport.url}") String baseUrl) {
        this.api = api;
        this.baseUrl = baseUrl;
    }

    public List<VehicleDto> getVehicles(String token, String type, String status) {
        return api.getList(ApiGateway.uri(baseUrl, "/vehicles", "type", type, "status", status),
                token, VehicleDto[].class);
    }

    public List<ParkingLotDto> getParking(String token, boolean onlyAvailable) {
        return api.getList(ApiGateway.uri(baseUrl, "/parking", "available", onlyAvailable ? "true" : null),
                token, ParkingLotDto[].class);
    }

    public ReservationDto reserve(String token, Integer parkingId, ReservationRequestDto request) {
        return api.post(ApiGateway.uri(baseUrl, "/parking/" + parkingId + "/reserve"), token, request,
                ReservationDto.class);
    }
}
