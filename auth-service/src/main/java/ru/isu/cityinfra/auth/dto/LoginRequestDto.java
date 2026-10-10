package ru.isu.cityinfra.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "Укажите имя пользователя")
    private String username;

    @NotBlank(message = "Укажите пароль")
    private String password;
}