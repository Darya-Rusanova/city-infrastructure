package ru.isu.cityinfra.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import ru.isu.cityinfra.frontend.client.UtilityClient;
import ru.isu.cityinfra.frontend.dto.IssueRequestDto;
import ru.isu.cityinfra.frontend.dto.IssueStatusUpdateDto;
import ru.isu.cityinfra.frontend.enums.IssueCategory;
import ru.isu.cityinfra.frontend.enums.IssueStatus;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final UtilityClient utilityClient;

    public IssueController(UtilityClient utilityClient) {
        this.utilityClient = utilityClient;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        String redirect = requireLogin(session);
        if (redirect != null) return redirect;

        try {
            model.addAttribute("issues", utilityClient.getAllIssues(token(session)));
        } catch (RestClientException e) {
            model.addAttribute("issues", java.util.List.of());
            model.addAttribute("error", "Не удалось получить список заявок: сервис недоступен");
        }
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("statuses", IssueStatus.values());
        return "issues/list";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        String redirect = requireLogin(session);
        if (redirect != null) return redirect;

        model.addAttribute("categories", IssueCategory.values());
        model.addAttribute("issueRequest", new IssueRequestDto());
        return "issues/form";
    }

    @PostMapping
    public String create(HttpSession session,
                          @ModelAttribute IssueRequestDto issueRequest,
                          Model model) {
        String redirect = requireLogin(session);
        if (redirect != null) return redirect;

        try {
            utilityClient.createIssue(token(session), issueRequest);
        } catch (RestClientException e) {
            model.addAttribute("categories", IssueCategory.values());
            model.addAttribute("issueRequest", issueRequest);
            model.addAttribute("error", "Не удалось создать заявку: " + e.getMessage());
            return "issues/form";
        }
        return "redirect:/issues";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(HttpSession session,
                                @PathVariable Integer id,
                                @RequestParam IssueStatus status,
                                Model model) {
        String redirect = requireLogin(session);
        if (redirect != null) return redirect;

        try {
            utilityClient.updateStatus(token(session), id, new IssueStatusUpdateDto(status));
        } catch (RestClientException e) {
            model.addAttribute("error", "Не удалось обновить статус заявки");
        }
        return "redirect:/issues";
    }

    private String requireLogin(HttpSession session) {
        return session.getAttribute("token") == null ? "redirect:/login" : null;
    }

    private String token(HttpSession session) {
        return (String) session.getAttribute("token");
    }
}
