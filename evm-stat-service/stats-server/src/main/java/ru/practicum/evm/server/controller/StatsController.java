package ru.practicum.evm.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evm.dto.EndpointHitDto;
import ru.practicum.evm.dto.ViewStatsDto;
import ru.practicum.evm.server.mapper.HitMapper;
import ru.practicum.evm.server.model.Hit;
import ru.practicum.evm.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.evm.server.util.Constants.DATE_FORMAT;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {

    private final StatsService statsService;
    private final HitMapper hitMapper;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о запросе: {}", endpointHitDto);
        Hit hit = statsService.create(hitMapper.fromDto(endpointHitDto));
        log.info("Запрос сохранен, присвоен id: {}", hit.getId());
        return hitMapper.toDto(hit);
    }

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStats(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
            @RequestParam(defaultValue = "") List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) throws BadRequestException {
        if(end.isBefore(start)) {
            throw new BadRequestException("end Before start");
        }
        log.info("Получен запрос статистики с параметрами: start={}, end={}, uris={}, unique={}",
                start, end, uris, unique);
        Collection<ViewStatsDto> stats = statsService.getStats(start, end, uris, unique);
        log.info("Количество полученных элементов в выборке: {}", stats.size());
        return stats;
    }
}
