package ru.isu.cityinfra.billing.enums;

public enum InvoiceStatus {
    UNPAID("Не оплачен"),
    PAID("Оплачен"),
    OVERDUE("Просрочен");

    private final String displayName;

    InvoiceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}