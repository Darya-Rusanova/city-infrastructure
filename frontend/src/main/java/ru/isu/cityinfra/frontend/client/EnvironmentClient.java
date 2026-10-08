package ru.isu.cityinfra.frontend.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.frontend.dto.SensorDataDto;
import ru.isu.cityinfra.frontend.dto.SensorDataRequestDto;
import ru.isu.cityinfra.frontend.dto.SensorDto;

@Component
public class EnvironmentClient {

    private final ApiGateway api;
    private final String baseUrl;

    public EnvironmentClient(ApiGateway api, @Value("${services.environment.url}") String baseUrl) {
        this.api = api;
        this.baseUrl = baseUrl;
    }

    public List<SensorDto> getSensors(String token, String type, String status) {
        return api.getList(ApiGateway.uri(baseUrl, "/sensors", "type", type, "status", status),
                token, SensorDto[].class);
    }

    public List<SensorDataDto> getSensorData(String token, Integer sensorId) {
        return api.getList(ApiGateway.uri(baseUrl, "/sensors/" + sensorId + "/data"),
                token, SensorDataDto[].class);
    }

    public SensorDataDto addReading(String token, SensorDataRequestDto request) {
        return api.post(ApiGateway.uri(baseUrl, "/sensors/data"), token, request, SensorDataDto.class);
    }
}
