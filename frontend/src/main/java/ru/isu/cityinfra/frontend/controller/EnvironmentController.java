package ru.isu.cityinfra.frontend.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.isu.cityinfra.frontend.client.EnvironmentClient;
import ru.isu.cityinfra.frontend.dto.SensorBar;
import ru.isu.cityinfra.frontend.dto.SensorDataDto;
import ru.isu.cityinfra.frontend.dto.SensorDataRequestDto;
import ru.isu.cityinfra.frontend.dto.SensorDto;
import ru.isu.cityinfra.frontend.exception.ApiException;
import ru.isu.cityinfra.frontend.security.Sessions;
import ru.isu.cityinfra.frontend.util.Labels;

@Controller
public class EnvironmentController {

    private static final int CHART_POINTS = 24;
    private static final DateTimeFormatter LABEL_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final EnvironmentClient environment;

    public EnvironmentController(EnvironmentClient environment) {
        this.environment = environment;
    }

    @GetMapping("/sensors")
    public String sensors(@RequestParam(required = false) String type,
                          @RequestParam(required = false) String status,
                          HttpSession session, Model model) {
        model.addAttribute("sensors", environment.getSensors(Sessions.token(session), type, status));
        model.addAttribute("sensorTypes", Labels.SENSOR_TYPES);
        model.addAttribute("sensorStatuses", Labels.SENSOR_STATUSES);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedStatus", status);
        return "environment/sensors";
    }

    @GetMapping("/sensors/{id}")
    public String sensor(@PathVariable Integer id, HttpSession session, Model model) {
        fillSensorPage(model, Sessions.token(session), id, new SensorDataRequestDto(id, null, null));
        return "environment/sensor";
    }

    @PostMapping("/sensors/{id}/data")
    public String addReading(@PathVariable Integer id,
                             @ModelAttribute("reading") SensorDataRequestDto reading,
                             HttpSession session, Model model, RedirectAttributes redirect) {
        String token = Sessions.token(session);
        String unit = reading.getUnit() == null || reading.getUnit().isBlank() ? null : reading.getUnit().trim();
        SensorDataRequestDto request = new SensorDataRequestDto(id, reading.getValue(), unit);
        try {
            environment.addReading(token, request);
            redirect.addFlashAttribute("success", "Показание сохранено");
            return "redirect:/sensors/" + id;
        } catch (ApiException e) {
            if (e.isUnauthorized()) {
                throw e;
            }
            model.addAttribute("error", e.getMessage());
            fillSensorPage(model, token, id, reading);
            return "environment/sensor";
        }
    }

    private void fillSensorPage(Model model, String token, Integer id, SensorDataRequestDto reading) {
        SensorDto sensor = environment.getSensors(token, null, null).stream()
                .filter(s -> id.equals(s.getId()))
                .findFirst()
                .orElseThrow(() -> new ApiException(404, "Датчик не найден"));
        List<SensorDataDto> data = environment.getSensorData(token, id);
        model.addAttribute("sensor", sensor);
        model.addAttribute("data", data);
        model.addAttribute("bars", buildBars(data));
        model.addAttribute("reading", reading);
    }

    /** Берет последние показания и считает высоту столбцов диаграммы в процентах. */
    private List<SensorBar> buildBars(List<SensorDataDto> data) {
        List<SensorDataDto> last = data.stream()
                .filter(d -> d.getValue() != null)
                .sorted(Comparator.comparing(SensorDataDto::getRecordedAt,
                        Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder())))
                .limit(CHART_POINTS)
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(last);

        double min = last.stream().mapToDouble(SensorDataDto::getValue).min().orElse(0);
        double max = last.stream().mapToDouble(SensorDataDto::getValue).max().orElse(0);

        List<SensorBar> bars = new ArrayList<>();
        for (SensorDataDto item : last) {
            double value = item.getValue();
            int height = max == min ? 60 : (int) Math.round(15 + 85 * (value - min) / (max - min));
            String label = item.getRecordedAt() == null ? "" : item.getRecordedAt().format(LABEL_FORMAT);
            bars.add(new SensorBar(label, value, height));
        }
        return bars;
    }
}
