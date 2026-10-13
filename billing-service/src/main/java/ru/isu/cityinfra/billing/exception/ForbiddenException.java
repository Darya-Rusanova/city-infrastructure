package ru.isu.cityinfra.billing.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) { super(message); }
}