package ru.isu.cityinfra.frontend.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Подписи для выпадающих списков фильтров. Значения совпадают с enum-ами бэкенд-сервисов.
 */
public final class Labels {

    public static final Map<String, String> VEHICLE_TYPES = of(
            "BUS", "Автобус", "TRAM", "Трамвай", "TAXI", "Такси");

    public static final Map<String, String> VEHICLE_STATUSES = of(
            "ACTIVE", "Активен", "INACTIVE", "Неактивен", "MAINTENANCE", "На обслуживании");

    public static final Map<String, String> SENSOR_TYPES = of(
            "AIR_QUALITY", "Качество воздуха", "NOISE", "Шум", "TEMPERATURE", "Температура");

    public static final Map<String, String> SENSOR_STATUSES = of(
            "ACTIVE", "Активен", "INACTIVE", "Неактивен", "MAINTENANCE", "На обслуживании");

    public static final Map<String, String> INVOICE_STATUSES = of(
            "UNPAID", "Не оплачен", "PAID", "Оплачен", "OVERDUE", "Просрочен");

    private Labels() {
    }

    private static Map<String, String> of(String... pairs) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i + 1 < pairs.length; i += 2) {
            map.put(pairs[i], pairs[i + 1]);
        }
        return Collections.unmodifiableMap(map);
    }
}
