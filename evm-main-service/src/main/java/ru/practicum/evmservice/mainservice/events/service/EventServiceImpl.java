package ru.practicum.evmservice.mainservice.events.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.client.StatClient;
import ru.practicum.evm.dto.ViewStatsDto;
import ru.practicum.evmservice.mainservice.categories.model.Category;
import ru.practicum.evmservice.mainservice.categories.repository.CategoryRepository;
import ru.practicum.evmservice.mainservice.events.dto.EventFullDto;
import ru.practicum.evmservice.mainservice.events.dto.EventShortDto;
import ru.practicum.evmservice.mainservice.events.dto.EventSort;
import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.events.dto.NewEventDto;
import ru.practicum.evmservice.mainservice.events.dto.StateAction;
import ru.practicum.evmservice.mainservice.events.dto.UpdateEventAdminRequest;
import ru.practicum.evmservice.mainservice.events.dto.UpdateEventRequest;
import ru.practicum.evmservice.mainservice.events.dto.UpdateEventUserRequest;
import ru.practicum.evmservice.mainservice.events.mapper.EventMapper;
import ru.practicum.evmservice.mainservice.events.model.Event;
import ru.practicum.evmservice.mainservice.events.repository.CustomEventRepository;
import ru.practicum.evmservice.mainservice.events.repository.EventRepository;
import ru.practicum.evmservice.mainservice.exceptions.ForbiddenException;
import ru.practicum.evmservice.mainservice.exceptions.NotFoundException;
import ru.practicum.evmservice.mainservice.users.model.User;
import ru.practicum.evmservice.mainservice.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private CustomEventRepository customEventRepository;
    private StatClient statClient;

    @Override
    public List<EventShortDto> getEvents(int userId, int from, int size) {
        User user = checkUser(userId);
        List<Event> events = eventRepository.findAllByInitiator_IdWithOffsetAndLimit(user.getId(), from, size);
        return EventMapper.INSTANCE.toShortDtos(events);
    }

    @Override
    public EventFullDto getEvent(Integer userId, Integer eventId) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        checkUserRights(userId, event);
        //TODO добавить обращение к сервису статистики и внедрить данные в EventFullDto
        return EventMapper.INSTANCE.toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        EventState state = event.getState();
        if (state == EventState.PUBLISHED) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        }
        checkUserRights(userId, event);
        if (updateEventUserRequest.getEventDate() != null) {
            checkEventDate(updateEventUserRequest.getEventDate());
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        updateEventFields(updateEventUserRequest, event);
        eventRepository.save(event);
        return EventMapper.INSTANCE.toFullDto(event);
    }

    @Override
    public List<EventFullDto> getEventsForAdminQuery(List<Integer> users,
                                                     List<EventState> states,
                                                     List<Integer> categories,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     int from,
                                                     int size) {

        List<Event> events = customEventRepository
                .findEventsByFilter(users, states, categories, rangeStart, rangeEnd, from, size);
        if (events.isEmpty()) {return List.of();}
        return EventMapper.INSTANCE.toFullDtos(events);
    }

    @Override
    public EventFullDto updateEventFromAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = checkEvent(eventId);
        if (updateEventAdminRequest.getEventDate() != null) {
            checkEventDate(updateEventAdminRequest.getEventDate());
            if (event.getPublishedDate() != null && updateEventAdminRequest.getEventDate().isBefore(event.getPublishedDate().minusHours(1))) {
                throw new ForbiddenException("Event date cannot be before published date minus 1 hours");
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT -> {
                    if (!event.getState().equals(EventState.PENDING)) {
                        throw new ForbiddenException(String
                                .format("Cannot publish the event because it's not in the right state: %s",
                                        updateEventAdminRequest.getStateAction()));
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedDate(LocalDateTime.now());
                }
                case REJECT_EVENT -> {
                    if (event.getState().equals(EventState.PUBLISHED)) {
                        throw new ForbiddenException(String
                                .format("Cannot reject the event because it's not in the right state: %s",
                                        EventState.PUBLISHED));
                    }
                    event.setState(EventState.CANCELED);
                }
            }
        }
        updateEventFields(updateEventAdminRequest, event);

        return EventMapper.INSTANCE.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getPublicEvent(int eventId, HttpServletRequest request) {
        Event event = eventRepository.findEventByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(
                        () -> new NotFoundException("Event with id=" + eventId + " was not found")
                );

        List<ViewStatsDto> views = statClient.getStat(event.getCreatedDate(), LocalDateTime.now(), List.of(request.getRequestURI()),true);

        EventFullDto eventFullDto = EventMapper.INSTANCE.toFullDto(event);
        eventFullDto.setViews(views.get(0).getHits().intValue());
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEventsWithFilters(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, int from, int size) {
        List<Event> events;
        if (categories != null) {
            if (paid != null) {
                if (onlyAvailable) {
                    events = eventRepository.getEventsWithFiltersOnlyAvailableOrderByEventDate(text, categories, paid, rangeStart, rangeEnd, from, size);
                } else {
                    events = eventRepository.getEventsWithFiltersOrderByEventDate(text, categories, paid, rangeStart, rangeEnd, from, size);
                }
            } else {
                if (onlyAvailable) {
                    events = eventRepository.getEventsWithFiltersOnlyAvailableOrderByEventDate(text, categories, rangeStart, rangeEnd, from, size);
                } else {
                    events = eventRepository.getEventsWithFiltersOrderByEventDate(text, categories, rangeStart, rangeEnd, from, size);
                }
            }
        } else {
            if (paid != null) {
                if (onlyAvailable) {
                    events = eventRepository.getEventsWithFiltersOnlyAvailableOrderByEventDate(text, paid, rangeStart, rangeEnd, from, size);
                } else {
                    events = eventRepository.getEventsWithFiltersOrderByEventDate(text, paid, rangeStart, rangeEnd, from, size);
                }
            } else {
                if (onlyAvailable) {
                    events = eventRepository.getEventsWithFiltersOnlyAvailableOrderByEventDate(text, rangeStart, rangeEnd, from, size);
                } else {
                    events = eventRepository.getEventsWithFiltersOrderByEventDate(text, rangeStart, rangeEnd, from, size);
                }
            }
        }


        //TODO добавить обращение к сервису статистики и внедрить данные

        switch (sort) {
            case EVENT_DATE -> {
                return EventMapper.INSTANCE.toShortDtos(events);
            }
            case VIEWS -> {
                List<EventShortDto> eventShortDtos = EventMapper.INSTANCE.toShortDtos(events);
                return eventShortDtos
                        .stream()
                        .sorted(Comparator.comparingInt(EventShortDto::getViews))
                        .collect(Collectors.toList());
            }
        }
        return List.of();
    }

    @Override
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        User user = checkUser(userId);
        Category category = checkCategory(newEventDto.getCategory());
        checkEventDate(newEventDto.getEventDate());
        newEventDto.setInitiatorId(userId);
        Event event = eventRepository.save(EventMapper.INSTANCE.toEvent(newEventDto));
        event.setInitiator(user);
        event.setCategory(category);

        //TODO добавить обращение к сервису статистики и внедрить данные в EventFullDto

        return EventMapper.INSTANCE.toFullDto(event);
    }

    private User checkUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Initiator with id=%s was not found", userId))
        );
    }

    private Event checkEvent(Integer eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id=%s was not found", eventId))
        );
    }

    private Category checkCategory(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    private void checkUserRights(Integer userId, Event event) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException(String.format("User with id=%s do not have access to full data", userId));
        }
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Field: eventDate. Error: Дата и время на которые намечено событие не может " +
                                         "быть раньше, чем через два часа от текущего момента. Value: " +
                                         eventDate.format(DateTimeFormatter.ISO_DATE_TIME));
        }
    }

    private void updateEventFields(UpdateEventRequest updateEventRequest, Event event) {
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = checkCategory(updateEventRequest.getCategory());
            event.setCategory(category);
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getLocation() != null) {
            event.getLocation().setLat(updateEventRequest.getLocation().getLat());
            event.getLocation().setLon(updateEventRequest.getLocation().getLon());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
    }


}
