package ru.isu.cityinfra.frontend.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.isu.cityinfra.frontend.client.TransportClient;
import ru.isu.cityinfra.frontend.dto.ParkingLotDto;
import ru.isu.cityinfra.frontend.dto.ReservationDto;
import ru.isu.cityinfra.frontend.dto.ReservationRequestDto;
import ru.isu.cityinfra.frontend.exception.ApiException;
import ru.isu.cityinfra.frontend.security.Sessions;
import ru.isu.cityinfra.frontend.util.Labels;

@Controller
public class TransportController {

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final TransportClient transport;

    public TransportController(TransportClient transport) {
        this.transport = transport;
    }

    @GetMapping("/transport")
    public String vehicles(@RequestParam(required = false) String type,
                           @RequestParam(required = false) String status,
                           HttpSession session, Model model) {
        model.addAttribute("vehicles", transport.getVehicles(Sessions.token(session), type, status));
        model.addAttribute("vehicleTypes", Labels.VEHICLE_TYPES);
        model.addAttribute("vehicleStatuses", Labels.VEHICLE_STATUSES);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedStatus", status);
        return "transport/vehicles";
    }

    @GetMapping("/parking")
    public String parking(@RequestParam(defaultValue = "false") boolean available,
                          HttpSession session, Model model) {
        model.addAttribute("parkingLots", transport.getParking(Sessions.token(session), available));
        model.addAttribute("onlyAvailable", available);
        return "transport/parking";
    }

    @GetMapping("/parking/{id}/reserve")
    public String reserveForm(@PathVariable Integer id, HttpSession session, Model model) {
        fillReserveForm(model, findParking(Sessions.token(session), id), new ReservationRequestDto());
        return "transport/reserve";
    }

    @PostMapping("/parking/{id}/reserve")
    public String reserve(@PathVariable Integer id,
                          @ModelAttribute("reservation") ReservationRequestDto reservation,
                          HttpSession session, Model model, RedirectAttributes redirect) {
        String token = Sessions.token(session);
        ParkingLotDto parking = findParking(token, id);
        ReservationRequestDto request = new ReservationRequestDto(
                normalize(reservation.getStartTime()), normalize(reservation.getEndTime()));
        try {
            ReservationDto created = transport.reserve(token, id, request);
            redirect.addFlashAttribute("success",
                    "Парковка «" + parking.getName() + "» забронирована. Номер брони: " + created.getId());
            return "redirect:/parking";
        } catch (ApiException e) {
            if (e.isUnauthorized()) {
                throw e;
            }
            model.addAttribute("error", e.getMessage());
            fillReserveForm(model, parking, reservation);
            return "transport/reserve";
        }
    }

    private ParkingLotDto findParking(String token, Integer id) {
        return transport.getParking(token, false).stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .orElseThrow(() -> new ApiException(404, "Парковка не найдена"));
    }

    private void fillReserveForm(Model model, ParkingLotDto parking, ReservationRequestDto reservation) {
        model.addAttribute("parking", parking);
        model.addAttribute("reservation", reservation);
        model.addAttribute("minTime", LocalDateTime.now().plusMinutes(1).format(INPUT_FORMAT));
    }

    /** Поле datetime-local отдает "2026-10-05T18:00", а бэкенд ждет секунды. */
    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.length() == 16 ? value + ":00" : value;
    }
}
