package ru.isu.cityinfra.auth.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("Пользователь"),
    ADMIN("Администратор");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}