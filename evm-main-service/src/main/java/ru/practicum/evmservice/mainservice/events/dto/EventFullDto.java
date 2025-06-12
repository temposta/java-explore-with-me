package ru.practicum.evmservice.mainservice.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.evmservice.mainservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.mainservice.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventFullDto {

    private String annotation;

    private CategoryDto category;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private Integer confirmedRequests;

    /**
     * Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Integer id;

    private UserShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

    private String title;

    private Integer views;
}
