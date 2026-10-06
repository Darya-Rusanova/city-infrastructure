package ru.isu.cityinfra.environment.enums;

public enum SensorType {
    AIR_QUALITY("Качество воздуха"),
    NOISE("Шум"),
    TEMPERATURE("Температура");

    private final String displayName;

    SensorType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}