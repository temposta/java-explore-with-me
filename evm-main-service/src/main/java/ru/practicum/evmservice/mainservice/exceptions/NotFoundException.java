package ru.practicum.evmservice.mainservice.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

public class NotFoundException extends RuntimeException {

    @Getter
    private final String reason;
    @Getter
    private final LocalDateTime timestamp;

    public NotFoundException(String message, String reason) {
        super(message);
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    public NotFoundException(String message) {
        this(message, "The required object was not found.");
    }
}
