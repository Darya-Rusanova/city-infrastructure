package ru.isu.cityinfra.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationRequestDto {
    private Integer userId;
    private String channel;
    private String title;
    private String message;
}