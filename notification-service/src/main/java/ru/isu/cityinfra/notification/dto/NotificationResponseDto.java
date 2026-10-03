package ru.isu.cityinfra.notification.dto;

import lombok.*;
import ru.isu.cityinfra.notification.enums.NotificationChannel;
import ru.isu.cityinfra.notification.enums.NotificationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationResponseDto {
    private Integer id;
    private Integer userId;
    private NotificationChannel channel;
    private String nameChannel;
    private String title;
    private String message;
    private NotificationStatus status;
    private String nameStatus;
    private LocalDateTime createdAt;
}
