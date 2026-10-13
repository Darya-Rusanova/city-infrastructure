package ru.isu.cityinfra.utility.event;

public record IssueNotificationEvent(Integer userId, String title, String message) {}
