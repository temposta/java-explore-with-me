package ru.practicum.evmservice.mainservice.events.service;

import ru.practicum.evmservice.mainservice.events.dto.EventFullDto;
import ru.practicum.evmservice.mainservice.events.dto.EventShortDto;
import ru.practicum.evmservice.mainservice.events.dto.EventSort;
import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.events.dto.NewEventDto;
import ru.practicum.evmservice.mainservice.events.dto.UpdateEventAdminRequest;
import ru.practicum.evmservice.mainservice.events.dto.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(int userId, int from, int size);

    EventFullDto getEvent(Integer userId, Integer eventId);

    EventFullDto patchEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getEventsForAdminQuery(List<Integer> users, List<EventState> states, List<Integer> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventFromAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto getPublicEvent(int eventId);

    List<EventShortDto> getEventsWithFilters(String text, List<Integer> categories, Boolean paid,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             Boolean onlyAvailable, EventSort sort, int from, int size);
}
