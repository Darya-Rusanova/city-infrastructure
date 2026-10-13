package ru.isu.cityinfra.frontend.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.isu.cityinfra.frontend.client.BillingClient;
import ru.isu.cityinfra.frontend.dto.InvoiceDto;
import ru.isu.cityinfra.frontend.dto.PaymentDto;
import ru.isu.cityinfra.frontend.dto.PaymentRequestDto;
import ru.isu.cityinfra.frontend.exception.ApiException;
import ru.isu.cityinfra.frontend.security.Sessions;
import ru.isu.cityinfra.frontend.util.Labels;

@Controller
public class BillingController {

    private final BillingClient billing;

    public BillingController(BillingClient billing) {
        this.billing = billing;
    }

    @GetMapping("/billing")
    public String invoices(@RequestParam(required = false) Integer userId,
                           @RequestParam(required = false) String status,
                           HttpSession session, Model model) {
        boolean admin = Sessions.isAdmin(session);
        // обычный пользователь видит только свои счета, администратор может указать любого
        Integer targetUser = admin && userId != null ? userId : Sessions.userId(session);
        List<InvoiceDto> invoices = billing.getInvoices(Sessions.token(session), targetUser, status);

        BigDecimal debt = invoices.stream()
                .filter(i -> "UNPAID".equals(i.getStatus()) || "OVERDUE".equals(i.getStatus()))
                .map(InvoiceDto::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("invoices", invoices);
        model.addAttribute("debt", debt);
        model.addAttribute("admin", admin);
        model.addAttribute("targetUser", targetUser);
        model.addAttribute("pending", pending(session));
        model.addAttribute("invoiceStatuses", Labels.INVOICE_STATUSES);
        model.addAttribute("selectedStatus", status);
        return "billing/invoices";
    }

    @PostMapping("/billing/pay")
    public String pay(@RequestParam Integer invoiceId, @RequestParam BigDecimal amount,
                      @RequestParam(required = false) Integer userId,
                      HttpSession session, RedirectAttributes redirect) {
        try {
            PaymentDto payment = billing.createPayment(Sessions.token(session),
                    new PaymentRequestDto(invoiceId, amount));
            pending(session).put(invoiceId, payment.getTransactionRef());
            redirect.addFlashAttribute("success",
                    "Платеж создан и ждет подтверждения платежной системы. Транзакция: " + payment.getTransactionRef());
        } catch (ApiException e) {
            if (e.isUnauthorized()) {
                throw e;
            }
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return back(userId);
    }

    /** Демонстрация: имитирует ответ платежной системы об успешной оплате. */
    @PostMapping("/billing/confirm")
    public String confirm(@RequestParam Integer invoiceId,
                          @RequestParam(required = false) Integer userId,
                          HttpSession session, RedirectAttributes redirect) {
        Map<Integer, String> pending = pending(session);
        String transactionRef = pending.get(invoiceId);
        if (transactionRef == null) {
            redirect.addFlashAttribute("error", "Нет платежа, ожидающего подтверждения");
            return back(userId);
        }
        try {
            billing.confirmPayment(transactionRef);
            pending.remove(invoiceId);
            redirect.addFlashAttribute("success", "Оплата подтверждена, счет оплачен");
        } catch (ApiException e) {
            // вебхук вызывается без токена пользователя, поэтому любые ошибки показываем как сообщение
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return back(userId);
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, String> pending(HttpSession session) {
        Object value = session.getAttribute(Sessions.PENDING_PAYMENTS);
        if (value == null) {
            Map<Integer, String> created = new HashMap<>();
            session.setAttribute(Sessions.PENDING_PAYMENTS, created);
            return created;
        }
        return (Map<Integer, String>) value;
    }

    private String back(Integer userId) {
        return userId == null ? "redirect:/billing" : "redirect:/billing?userId=" + userId;
    }
}
