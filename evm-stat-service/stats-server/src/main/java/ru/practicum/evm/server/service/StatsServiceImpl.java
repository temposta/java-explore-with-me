package ru.practicum.evm.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.ViewStatsDto;
import ru.practicum.evm.server.model.Hit;
import ru.practicum.evm.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitRepository hitRepository;

    @Override
    public Hit create(Hit hit) {
        return hitRepository.save(hit);
    }

    @Override
    public Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                             List<String> uris, Boolean unique) {
        if (uris.isEmpty()) {
            if (unique) {
                return hitRepository.findUniqueViewStatsBetweenStartEnd(start, end);
            } else {
                return hitRepository.findNotUniqueViewStatsBetweenStartEnd(start, end);
            }
        } else {
            if (unique) {
                return hitRepository.findUniqueViewStatsBetweenStartEndWithFilterUri(start, end, uris);
            } else {
                return hitRepository.findNotUniqueViewStatsBetweenStartEndWithFilterUri(start, end, uris);
            }
        }
    }
}
