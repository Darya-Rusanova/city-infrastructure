package ru.isu.cityinfra.auth.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}