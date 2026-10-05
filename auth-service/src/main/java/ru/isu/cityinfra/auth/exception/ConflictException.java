package ru.isu.cityinfra.auth.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}