package ru.practicum.evmservice.mainservice.exceptions;

public class IncorrectRequestException extends NotFoundException {

    public IncorrectRequestException(String message, String reason) {
        super(message, reason);
    }

    public IncorrectRequestException(String message) {
        this(message, "Incorrectly made request.");
    }
}
