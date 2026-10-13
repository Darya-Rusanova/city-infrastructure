package ru.isu.cityinfra.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPublicDto {
    private Integer id;
    private String username;
    private String fullName;
}