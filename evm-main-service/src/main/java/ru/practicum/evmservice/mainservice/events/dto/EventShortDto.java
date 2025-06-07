package ru.practicum.evmservice.mainservice.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.evmservice.mainservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.mainservice.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventShortDto {

    private String annotation;

    private CategoryDto category;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Integer id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;
}
