package ru.practicum.evmservice.mainservice.events.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

/**
 * Широта и долгота места проведения события
 */
@Data
@Getter
public class LocationDto {

    /**
     * Широта
     */
    @NotNull
    private Float lat;

    /**
     * Долгота
     */
    @NotNull
    private Float lon;
}
