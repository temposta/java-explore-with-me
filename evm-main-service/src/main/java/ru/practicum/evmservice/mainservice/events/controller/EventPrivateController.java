package ru.practicum.evmservice.mainservice.events.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.events.dto.EventFullDto;
import ru.practicum.evmservice.mainservice.events.dto.EventShortDto;
import ru.practicum.evmservice.mainservice.events.dto.NewEventDto;
import ru.practicum.evmservice.mainservice.events.dto.UpdateEventUserRequest;
import ru.practicum.evmservice.mainservice.events.service.EventService;
import ru.practicum.evmservice.mainservice.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.evmservice.mainservice.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.evmservice.mainservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.mainservice.requests.service.RequestService;

import java.util.List;

/**
 * Закрытый API для работы с событиями
 */
@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    /**
     * Получение событий, добавленных текущим пользователем
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     *
     * @param userId id текущего пользователя
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     *               Default value : 0
     * @param size   количество элементов в наборе
     *               Default value : 10
     * @return список отобранных событий
     */
    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable int userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Get events from {}, with size {}", from, size);
        return eventService.getEvents(userId, from, size);
    }

    /**
     * Добавление нового события.
     * <p>
     * Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     *
     * @param userId      id текущего пользователя
     * @param newEventDto данные добавляемого события
     * @return данные добавленного события в формате EventFullDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Integer userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Creating new event {} from user with id {}", newEventDto, userId);
        return eventService.createEvent(userId, newEventDto);
    }

    /**
     * Получение полной информации о событии, добавленном текущим пользователем
     * <p>
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return полная информация о событии с указанным id
     */
    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Integer userId,
                                 @PathVariable Integer eventId) {
        log.info("Get event {} from user with id {}", eventId, userId);
        return eventService.getEvent(userId, eventId);
    }

    /**
     * Изменение события, добавленного текущим пользователем
     * <p>
     * Изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)
     * <p>
     * Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)
     *
     * @param userId                 id текущего пользователя
     * @param eventId                id редактируемого события
     * @param updateEventUserRequest Новые данные события
     * @return полная информация о событии после обновления
     */
    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable Integer userId,
                                   @PathVariable Integer eventId,
                                   @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Patch event {} from user with id {}", eventId, userId);
        return eventService.patchEvent(userId, eventId, updateEventUserRequest);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return найденные запросы на участие
     */
    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable int userId,
                                                     @PathVariable Integer eventId) {
        log.info("Get requests for event {} from user with id {}", eventId, userId);
        return requestService.getRequestsOnEvent(userId, eventId);
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     * <p>
     * Если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
     * нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
     * <p>
     * Статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
     * <p>
     * Если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
     *
     * @param userId                          id текущего пользователя
     * @param eventId                         id события текущего пользователя
     * @param eventRequestStatusUpdateRequest Новый статус для заявок на участие в событии текущего пользователя
     * @return Измененные статусы заявок
     */
    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable int userId,
                                                               @PathVariable Integer eventId,
                                                               @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Update requests for event {} from user with id {}", eventId, userId);
        return requestService.updateRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
