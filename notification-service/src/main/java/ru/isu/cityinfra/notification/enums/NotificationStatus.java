package ru.isu.cityinfra.notification.enums;

public enum NotificationStatus {
    SENT("Отправлено"),
    FAILED("Не отправлено");

    private final String displayName;

    NotificationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
