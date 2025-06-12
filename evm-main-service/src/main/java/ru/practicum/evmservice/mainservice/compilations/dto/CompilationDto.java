package ru.practicum.evmservice.mainservice.compilations.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.evmservice.mainservice.events.dto.EventShortDto;

import java.util.List;

@Builder
@Getter
@Setter
@Data
public class CompilationDto {

    private final List<EventShortDto> events;

    private final Integer id;

    private final Boolean pinned;

    private final String title;
}
