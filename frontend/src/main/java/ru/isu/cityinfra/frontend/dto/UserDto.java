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
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String nameRole;
    private LocalDateTime createdAt;
}
