package ru.isu.cityinfra.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.isu.cityinfra.frontend.client.UtilityClient;
import ru.isu.cityinfra.frontend.dto.IssueRequestDto;
import ru.isu.cityinfra.frontend.dto.IssueStatusUpdateDto;
import ru.isu.cityinfra.frontend.enums.IssueCategory;
import ru.isu.cityinfra.frontend.enums.IssueStatus;
import ru.isu.cityinfra.frontend.exception.ApiException;
import ru.isu.cityinfra.frontend.security.Sessions;

import java.util.List;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final UtilityClient utilityClient;

    public IssueController(UtilityClient utilityClient) {
        this.utilityClient = utilityClient;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String status,
                       @RequestParam(required = false) String category,
                       @RequestParam(defaultValue = "new") String sort,
                       HttpSession session, Model model) {
        // в сервис уходят только значения из enum, произвольный текст из адресной строки отбрасывается
        IssueStatus statusFilter = parseStatus(status);
        IssueCategory categoryFilter = parseCategory(category);
        boolean oldFirst = "old".equals(sort);

        try {
            model.addAttribute("issues", utilityClient.getIssues(Sessions.token(session),
                    statusFilter == null ? null : statusFilter.name(),
                    categoryFilter == null ? null : categoryFilter.name(),
                    "createdAt", oldFirst ? "asc" : "desc"));
        } catch (ApiException e) {
            if (e.isUnauthorized()) {
                throw e;
            }
            model.addAttribute("issues", List.of());
            model.addAttribute("error", "Не удалось получить список заявок: " + e.getMessage());
        }
        model.addAttribute("statuses", IssueStatus.values());
        model.addAttribute("categories", IssueCategory.values());
        model.addAttribute("selectedStatus", statusFilter == null ? "" : statusFilter.name());
        model.addAttribute("selectedCategory", categoryFilter == null ? "" : categoryFilter.name());
        model.addAttribute("selectedSort", oldFirst ? "old" : "new");
        return "issues/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("categories", IssueCategory.values());
        model.addAttribute("issueRequest", new IssueRequestDto());
        return "issues/form";
    }

    @PostMapping
    public String create(@ModelAttribute("issueRequest") IssueRequestDto issueRequest,
                         HttpSession session, Model model, RedirectAttributes redirect) {
        try {
            utilityClient.createIssue(Sessions.token(session), issueRequest);
            redirect.addFlashAttribute("success", "Заявка отправлена");
            return "redirect:/issues";
        } catch (ApiException e) {
            if (e.isUnauthorized()) {
                throw e;
            }
            model.addAttribute("categories", IssueCategory.values());
            model.addAttribute("error", e.getMessage());
            return "issues/form";
        }
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Integer id, @RequestParam IssueStatus status,
                               HttpSession session, RedirectAttributes redirect) {
        try {
            utilityClient.updateStatus(Sessions.token(session), id, new IssueStatusUpdateDto(status));
            redirect.addFlashAttribute("success", "Статус заявки " + id + " обновлен");
        } catch (ApiException e) {
            if (e.isUnauthorized()) {
                throw e;
            }
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/issues";
    }

    private static IssueStatus parseStatus(String value) {
        try {
            return value == null || value.isBlank() ? null : IssueStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static IssueCategory parseCategory(String value) {
        try {
            return value == null || value.isBlank() ? null : IssueCategory.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
