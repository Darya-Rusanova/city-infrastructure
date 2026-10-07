package ru.isu.cityinfra.billing.enums;

public enum PaymentStatus {
    PENDING("В обработке"),
    SUCCESS("Успешно"),
    FAILED("Ошибка");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}