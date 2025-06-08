package ru.practicum.evm.client;

import ru.practicum.evm.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatClient {

    void postHit(String app, String uri, String ip, LocalDateTime timestamp);

    List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}