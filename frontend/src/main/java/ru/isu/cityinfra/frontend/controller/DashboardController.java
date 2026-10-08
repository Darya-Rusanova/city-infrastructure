package ru.isu.cityinfra.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.isu.cityinfra.frontend.client.BillingClient;
import ru.isu.cityinfra.frontend.client.EnvironmentClient;
import ru.isu.cityinfra.frontend.client.NotificationClient;
import ru.isu.cityinfra.frontend.client.TransportClient;
import ru.isu.cityinfra.frontend.client.UtilityClient;
import ru.isu.cityinfra.frontend.dto.ParkingLotDto;
import ru.isu.cityinfra.frontend.exception.ApiException;
import ru.isu.cityinfra.frontend.security.Sessions;

import java.util.function.Supplier;

/**
 * Главная страница после входа: сводка из всех сервисов. Если один сервис недоступен,
 * его плитка показывает прочерк, а остальные работают.
 */
@Controller
public class DashboardController {

    private final UtilityClient utility;
    private final TransportClient transport;
    private final BillingClient billing;
    private final EnvironmentClient environment;
    private final NotificationClient notifications;

    public DashboardController(UtilityClient utility, TransportClient transport, BillingClient billing,
                               EnvironmentClient environment, NotificationClient notifications) {
        this.utility = utility;
        this.transport = transport;
        this.billing = billing;
        this.environment = environment;
        this.notifications = notifications;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String token = Sessions.token(session);
        Integer userId = Sessions.userId(session);

        model.addAttribute("issuesCount",
                metric(() -> utility.getIssues(token, null, null, null, null).size()));
        model.addAttribute("freeSpots",
                metric(() -> transport.getParking(token, false).stream()
                        .map(ParkingLotDto::getAvailableSpots)
                        .filter(java.util.Objects::nonNull)
                        .mapToInt(Integer::intValue).sum()));
        model.addAttribute("unpaidCount",
                metric(() -> (int) billing.getInvoices(token, userId, null).stream()
                        .filter(i -> "UNPAID".equals(i.getStatus()) || "OVERDUE".equals(i.getStatus()))
                        .count()));
        model.addAttribute("activeSensors",
                metric(() -> environment.getSensors(token, null, "ACTIVE").size()));
        model.addAttribute("notificationsCount",
                metric(() -> notifications.getMy(token).size()));
        return "dashboard";
    }

    /** Возвращает число или null, если сервис не ответил. Просроченный токен отправляет на вход. */
    private Integer metric(Supplier<Integer> source) {
        try {
            return source.get();
        } catch (ApiException e) {
            if (e.isUnauthorized()) {
                throw e;
            }
            return null;
        }
    }
}
