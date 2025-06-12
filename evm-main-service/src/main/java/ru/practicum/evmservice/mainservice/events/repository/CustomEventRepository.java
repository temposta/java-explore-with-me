package ru.practicum.evmservice.mainservice.events.repository;

import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEventRepository {

    List<Event> findEventsByFilter(List<Integer> users, List<EventState> states, List<Integer> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);
}
