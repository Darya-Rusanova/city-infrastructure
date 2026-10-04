package ru.isu.cityinfra.utility.client;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.isu.cityinfra.utility.dto.NotificationRequestDto;

@Component
@Slf4j
public class NotificationClient {
    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);
    final RestTemplate restTemplate;
    final String notificationUrl;

    public NotificationClient(RestTemplate restTemplate,
                              @Value("${notification.service-url}") String notificationUrl){

        this.restTemplate = restTemplate;
        this.notificationUrl = notificationUrl;
    }
    public void send(Integer userId, String title, String message){
        try {
            NotificationRequestDto request = new NotificationRequestDto(userId, "IN_APP", title, message);
            restTemplate.postForEntity(notificationUrl,request,Void.class);
            log.info("сообщение отправлено пользователю {}",userId);
        } catch (Exception e) {
            log.error("сообщение не было отправлено пользователю {}: {}",userId,e.getMessage());
        }
    }
}
