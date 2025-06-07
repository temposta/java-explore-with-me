package ru.practicum.evmservice.mainservice.requests.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.mainservice.requests.service.RequestService;

import java.util.List;

/**
 * Закрытый API для работы с запросами текущего пользователя на участие в событиях
 */
@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@AllArgsConstructor
public class RequestPrivateController {
    RequestService requestService;

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     *
     * @param userId id текущего пользователя
     * @return список заявок текущего пользователя
     */
    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable int userId) {
        log.info("Get requests for user id {}", userId);
        return requestService.getRequests(userId);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     * <p>
     * Нельзя добавить повторный запрос (Ожидается код ошибки 409)
     * <p>
     * Инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
     * <p>
     * Нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
     * <p>
     * Если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
     * <p>
     * Если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти
     * в состояние подтвержденного
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return данные созданной заявки на участие в событии
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable int userId,
                                                 @RequestParam int eventId) {
        log.info("Create request for user id {} on event id {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId    id текущего пользователя
     * @param requestId id запроса на участие
     * @return информация об отмененном событии
     */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable int userId,
                                                 @PathVariable int requestId) {
        log.info("Cancel request for user id {} on request id {}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }

}
