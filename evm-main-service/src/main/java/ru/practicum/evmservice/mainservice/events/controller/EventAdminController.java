package ru.practicum.evmservice.mainservice.events.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.events.dto.EventFullDto;
import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.events.dto.UpdateEventAdminRequest;
import ru.practicum.evmservice.mainservice.events.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * API для работы с событиями
 */
@Slf4j
@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    /**
     * Поиск событий
     *
     * @param users      список id пользователей, чьи события нужно найти
     * @param states     список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd   дата и время не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора
     *                   Default value : 0
     * @param size       количество событий в наборе
     *                   Default value : 10
     * @return список отобранных событий
     */
    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<EventState> states,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("getEvents called");
        return eventService.getEventsForAdminQuery(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Редактирование данных события и его статуса (отклонение, публикация)
     * <p>
     * Дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
     * <p>
     * Событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
     * <p>
     * Событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
     *
     * @param eventId                 id события
     * @param updateEventAdminRequest Данные для изменения информации о событии
     * @return данные отредактированного события
     */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEventFromAdmin(@PathVariable int eventId,
                                             @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("updateEventFromAdmin called");
        return eventService.updateEventFromAdmin(eventId, updateEventAdminRequest);
    }

}
