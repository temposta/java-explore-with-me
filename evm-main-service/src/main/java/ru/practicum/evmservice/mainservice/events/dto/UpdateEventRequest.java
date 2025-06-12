package ru.practicum.evmservice.mainservice.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateEventRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Integer category;

    @Size(min = 20, max = 7000)
    private String description;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @Positive
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    private String title;
}
