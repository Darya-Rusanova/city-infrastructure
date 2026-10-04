package ru.isu.cityinfra.transport.enums;

public enum VehicleStatus {
    ACTIVE("Активен"),
    INACTIVE("Неактивен"),
    MAINTENANCE("На обслуживании");

    private final String displayName;

    VehicleStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}