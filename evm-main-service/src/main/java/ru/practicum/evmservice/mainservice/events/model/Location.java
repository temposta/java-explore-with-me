package ru.practicum.evmservice.mainservice.events.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Широта и долгота места проведения события
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {

    /**
     * Широта
     */
    @NotNull
    @Column(nullable = false)
    private Float lat;

    /**
     * Долгота
     */
    @NotNull
    @Column(nullable = false)
    private Float lon;
}
