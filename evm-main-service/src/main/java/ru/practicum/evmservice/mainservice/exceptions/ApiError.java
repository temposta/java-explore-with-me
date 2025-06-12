package ru.practicum.evmservice.mainservice.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Общий формат для возврата ошибок Api проекта
 */
@Builder
public class ApiError {

    /**
     * Список стектрейсов или описания ошибок.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final List<String> errors;

    /**
     * Сообщение об ошибке.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final String message;

    /**
     * Общее описание причины ошибки.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final String reason;

    /**
     * Код статуса HTTP-ответа.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final HttpStatus status;

    /**
     * Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss").
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
}
