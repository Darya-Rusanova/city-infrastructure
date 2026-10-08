package ru.isu.cityinfra.frontend.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Integer id;
    private Integer userId;
    private String channel;
    private String nameChannel;
    private String title;
    private String message;
    private String status;
    private String nameStatus;
    private LocalDateTime createdAt;
}
