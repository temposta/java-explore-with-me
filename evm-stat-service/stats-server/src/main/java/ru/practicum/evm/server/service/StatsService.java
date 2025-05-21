package ru.practicum.evm.server.service;

import ru.practicum.evm.dto.ViewStatsDto;
import ru.practicum.evm.server.model.Hit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsService {

    Hit create(Hit hit);

    Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                      List<String> uris, Boolean unique);
}
