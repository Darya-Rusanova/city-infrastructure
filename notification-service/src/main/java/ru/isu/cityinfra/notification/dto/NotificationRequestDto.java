package ru.isu.cityinfra.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationRequestDto {

    @NotNull(message = "Укажите получателя")
    private Integer userId;

    private String channel;

    @NotBlank(message = "Укажите заголовок")
    private String title;

    @NotBlank(message = "Укажите текст сообщения")
    private String message;
}
