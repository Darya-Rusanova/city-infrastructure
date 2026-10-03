package ru.isu.cityinfra.transport.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.transport.enums.VehicleStatus;
import ru.isu.cityinfra.transport.enums.VehicleType;
import ru.isu.cityinfra.transport.model.ParkingLot;
import ru.isu.cityinfra.transport.model.Vehicle;
import ru.isu.cityinfra.transport.repository.ParkingRepository;
import ru.isu.cityinfra.transport.repository.VehicleRepository;

import java.time.LocalDateTime;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ParkingRepository parkingRepository;

    @Override
    public void run(String... args) {
        initVehicles();
        initParkingLots();
    }

    private void initVehicles() {
        if (vehicleRepository.count() > 0) {
            log.info("Vehicles already initialized: {} records", vehicleRepository.count());
            return;
        }

        log.info("Initializing vehicles...");

        vehicleRepository.save(createVehicle(VehicleType.BUS, 52.2896, 104.2807, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.BUS, 52.2850, 104.2900, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.BUS, 52.2820, 104.2750, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.BUS, 52.2870, 104.2850, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.BUS, 52.2910, 104.2820, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.BUS, 52.2840, 104.2780, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.BUS, 52.2880, 104.2920, VehicleStatus.INACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.BUS, 52.2900, 104.2770, VehicleStatus.MAINTENANCE));

        vehicleRepository.save(createVehicle(VehicleType.TRAM, 52.2800, 104.2750, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.TRAM, 52.2830, 104.2790, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.TRAM, 52.2870, 104.2830, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.TRAM, 52.2820, 104.2810, VehicleStatus.INACTIVE));

        vehicleRepository.save(createVehicle(VehicleType.TAXI, 52.2900, 104.2850, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.TAXI, 52.2880, 104.2820, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.TAXI, 52.2860, 104.2880, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.TAXI, 52.2840, 104.2840, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.TAXI, 52.2890, 104.2790, VehicleStatus.ACTIVE));
        vehicleRepository.save(createVehicle(VehicleType.TAXI, 52.2920, 104.2860, VehicleStatus.INACTIVE));

        log.info("Vehicles initialized: {} records", vehicleRepository.count());
    }

    private void initParkingLots() {
        if (parkingRepository.count() > 0) {
            log.info("Parking lots already initialized: {} records", parkingRepository.count());
            return;
        }

        log.info("Initializing parking lots...");

        parkingRepository.save(createParkingLot(
                "Парковка ТЦ «Модный квартал»",
                52.2869, 104.3050,
                200, 45
        ));

        parkingRepository.save(createParkingLot(
                "Парковка у ж/д вокзала",
                52.2830, 104.2790,
                150, 87
        ));

        parkingRepository.save(createParkingLot(
                "Парковка в центре (пл. Кирова)",
                52.2870, 104.2800,
                80, 12
        ));

        parkingRepository.save(createParkingLot(
                "Парковка у парка им. Горького",
                52.2880, 104.2870,
                50, 50
        ));

        parkingRepository.save(createParkingLot(
                "Парковка ТЦ «Сильвер Молл»",
                52.2910, 104.2820,
                300, 128
        ));

        parkingRepository.save(createParkingLot(
                "Парковка у стадиона «Труд»",
                52.2850, 104.2900,
                120, 0
        ));

        parkingRepository.save(createParkingLot(
                "Парковка у аэропорта",
                52.2680, 104.3880,
                250, 175
        ));

        parkingRepository.save(createParkingLot(
                "Парковка у Ботанического сада",
                52.2820, 104.2750,
                40, 8
        ));

        log.info("Parking lots initialized: {} records", parkingRepository.count());

    }

    private Vehicle createVehicle(VehicleType type, double lat, double lon, VehicleStatus status) {
        Vehicle v = new Vehicle();
        v.setType(type);
        v.setLatitude(lat);
        v.setLongitude(lon);
        v.setStatus(status);
        v.setUpdatedAt(LocalDateTime.now());
        return v;
    }

    private ParkingLot createParkingLot(String name, double lat, double lon,
                                        int totalSpots, int availableSpots) {
        ParkingLot p = new ParkingLot();
        p.setName(name);
        p.setLatitude(lat);
        p.setLongitude(lon);
        p.setTotalSpots(totalSpots);
        p.setAvailableSpots(availableSpots);
        return p;
    }

}