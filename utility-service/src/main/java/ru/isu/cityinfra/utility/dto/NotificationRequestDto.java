package ru.isu.cityinfra.utility.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private Integer userId;
    private String channel;
    private String title;
    private String message;
}
