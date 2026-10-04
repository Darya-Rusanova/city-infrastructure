package ru.isu.cityinfra.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Укажите имя пользователя")
    @Size(min = 3, max = 50, message = "Имя пользователя от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Укажите email")
    @Email(message = "Некорректный email")
    @Size(max = 100, message = "Email слишком длинный")
    private String email;

    @NotBlank(message = "Укажите пароль")
    @Size(min = 6, max = 100, message = "Пароль от 6 до 100 символов")
    private String password;

    @Size(max = 100, message = "ФИО слишком длинное")
    private String fullName;
}