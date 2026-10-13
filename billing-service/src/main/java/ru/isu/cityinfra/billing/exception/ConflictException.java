package ru.isu.cityinfra.billing.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}