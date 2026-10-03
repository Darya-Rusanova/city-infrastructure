package ru.isu.cityinfra.transport.enums;

public enum VehicleType {
    BUS("Автобус"),
    TRAM("Трамвай"),
    TAXI("Такси");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}