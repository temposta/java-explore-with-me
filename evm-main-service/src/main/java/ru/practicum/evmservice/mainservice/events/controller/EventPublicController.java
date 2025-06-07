package ru.practicum.evmservice.mainservice.events.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.events.dto.EventFullDto;
import ru.practicum.evmservice.mainservice.events.dto.EventShortDto;
import ru.practicum.evmservice.mainservice.events.dto.EventSort;
import ru.practicum.evmservice.mainservice.events.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Публичный API для работы с событиями
 */
@Slf4j
@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventPublicController {
    private final EventService eventService;

    /**
     * Получение событий с возможностью фильтрации
     * <p>
     * Это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
     * <p>
     * Текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
     * <p>
     * Если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
     * <p>
     * Информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
     * <p>
     * Информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий в которых будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время не раньше которых должно произойти событие
     * @param rangeEnd      дата и время не позже которых должно произойти событие
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие
     *                      Default value : false
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров
     *                      Available values : EVENT_DATE, VIEWS
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора
     *                      Default value : 0
     * @param size          количество событий в наборе
     *                      Default value : 10
     * @return список отобранных событий в формате EventShortDto
     */
    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(defaultValue = "%") String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Future @Valid LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) EventSort sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("getEvents from public called");
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.parse("9999-12-31T23:59:59");
        }
//        if (rangeEnd.isBefore(rangeStart)) {
//            throw new MethodArgumentNotValidException("Дата окончания интервала поиска должна быть позже даты начала поиска");
//        }
        if (sort == null) {
            sort = EventSort.EVENT_DATE;
        }
        return eventService.getEventsWithFilters(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     * <p>
     * Событие должно быть опубликовано
     * <p>
     * Информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
     * <p>
     * Информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
     *
     * @param eventId id события
     * @return информация о запрашиваемом событии
     */
    @GetMapping("/{eventId}")
    public EventFullDto getPublicEvent(@PathVariable int eventId) {
        log.info("getEvent from public called");
        return eventService.getPublicEvent(eventId);
    }

}
