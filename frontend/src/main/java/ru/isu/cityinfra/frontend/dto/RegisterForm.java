package ru.isu.cityinfra.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;
}
