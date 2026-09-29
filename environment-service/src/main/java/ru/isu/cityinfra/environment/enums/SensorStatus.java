package ru.isu.cityinfra.environment.enums;

public enum SensorStatus {
    ACTIVE("Активен"),
    INACTIVE("Неактивен"),
    MAINTENANCE("На обслуживании");

    private final String displayName;

    SensorStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}