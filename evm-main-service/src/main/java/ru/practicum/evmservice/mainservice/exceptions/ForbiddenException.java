package ru.practicum.evmservice.mainservice.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

public class ForbiddenException extends RuntimeException {

    @Getter
    private final String reason;
    @Getter
    private final LocalDateTime timestamp;

    public ForbiddenException(String message, String reason) {
        super(message);
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    public ForbiddenException(String message) {
        this(message, "For the requested operation the conditions are not met.");
    }
}
