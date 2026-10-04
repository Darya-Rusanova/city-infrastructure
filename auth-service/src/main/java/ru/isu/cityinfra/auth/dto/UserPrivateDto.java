package ru.isu.cityinfra.auth.dto;

import lombok.*;
import ru.isu.cityinfra.auth.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrivateDto {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private String nameRole;
    private LocalDateTime createdAt;
}