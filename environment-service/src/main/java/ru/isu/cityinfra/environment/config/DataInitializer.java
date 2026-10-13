package ru.isu.cityinfra.environment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.environment.enums.SensorStatus;
import ru.isu.cityinfra.environment.enums.SensorType;
import ru.isu.cityinfra.environment.model.Sensor;
import ru.isu.cityinfra.environment.model.SensorData;
import ru.isu.cityinfra.environment.repository.SensorDataRepository;
import ru.isu.cityinfra.environment.repository.SensorRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private SensorDataRepository sensorDataRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) {
        if (sensorRepository.count() > 0) {
            log.info("Sensors already initialized: {} records", sensorRepository.count());
            return;
        }

        log.info("Initializing sensors...");

        Sensor s1 = sensorRepository.save(createSensor(
                "Датчик качества воздуха у пл. Кирова",
                SensorType.AIR_QUALITY, 52.2870, 104.2800, SensorStatus.ACTIVE));
        Sensor s2 = sensorRepository.save(createSensor(
                "Датчик шума у ж/д вокзала",
                SensorType.NOISE, 52.2830, 104.2790, SensorStatus.ACTIVE));
        Sensor s3 = sensorRepository.save(createSensor(
                "Датчик температуры в центре",
                SensorType.TEMPERATURE, 52.2869, 104.3050, SensorStatus.ACTIVE));
        Sensor s4 = sensorRepository.save(createSensor(
                "Датчик качества воздуха у ТЦ «Сильвер Молл»",
                SensorType.AIR_QUALITY, 52.2910, 104.2820, SensorStatus.ACTIVE));
        Sensor s5 = sensorRepository.save(createSensor(
                "Датчик шума у парка им. Горького",
                SensorType.NOISE, 52.2880, 104.2870, SensorStatus.ACTIVE));
        // Специально выключенный датчик — для проверки 409 при записи
        Sensor s6 = sensorRepository.save(createSensor(
                "Датчик температуры (на обслуживании)",
                SensorType.TEMPERATURE, 52.2820, 104.2750, SensorStatus.INACTIVE));

        initDataForSensor(s1, "ppm", 30.0, 80.0);
        initDataForSensor(s2, "dB", 40.0, 90.0);
        initDataForSensor(s3, "°C", -15.0, 30.0);
        initDataForSensor(s4, "ppm", 30.0, 80.0);
        initDataForSensor(s5, "dB", 40.0, 90.0);
        initDataForSensor(s6, "°C", -15.0, 30.0);

        log.info("Sensors initialized: {} records", sensorRepository.count());
        log.info("Sensor data initialized: {} records", sensorDataRepository.count());
    }

    private void initDataForSensor(Sensor sensor, String unit, double min, double max) {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 15; i++) {
            SensorData d = new SensorData();
            d.setSensorId(sensor.getId());
            d.setValue(Math.round((min + random.nextDouble() * (max - min)) * 10.0) / 10.0);
            d.setUnit(unit);
            d.setRecordedAt(now.minusHours(i));
            sensorDataRepository.save(d);
        }
    }

    private Sensor createSensor(String name, SensorType type,
                                double lat, double lon, SensorStatus status) {
        Sensor s = new Sensor();
        s.setName(name);
        s.setType(type);
        s.setLatitude(lat);
        s.setLongitude(lon);
        s.setStatus(status);
        return s;
    }
}