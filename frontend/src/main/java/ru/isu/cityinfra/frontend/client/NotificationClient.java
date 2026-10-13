package ru.isu.cityinfra.frontend.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.frontend.dto.NotificationDto;

@Component
public class NotificationClient {

    private final ApiGateway api;
    private final String baseUrl;

    public NotificationClient(ApiGateway api, @Value("${services.notification.url}") String baseUrl) {
        this.api = api;
        this.baseUrl = baseUrl;
    }

    public List<NotificationDto> getMy(String token) {
        return api.getList(ApiGateway.uri(baseUrl, "/notifications/my"), token, NotificationDto[].class);
    }
}
