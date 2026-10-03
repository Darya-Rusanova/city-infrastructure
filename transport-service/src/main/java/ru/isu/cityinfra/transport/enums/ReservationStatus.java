package ru.isu.cityinfra.transport.enums;

public enum ReservationStatus {
    ACTIVE("Активна"),
    CANCELLED("Отменена"),
    COMPLETED("Завершена");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
