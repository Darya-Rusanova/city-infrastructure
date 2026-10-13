package ru.isu.cityinfra.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPublicDto {
    private Integer id;
    private String username;
    private String fullName;
}
