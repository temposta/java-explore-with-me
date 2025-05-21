package ru.practicum.evm.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evm.dto.ViewStatsDto;
import ru.practicum.evm.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {

    @Query("select new ru.practicum.evm.dto.ViewStatsDto(hit.app, hit.uri, count(distinct hit.ip)) " +
           "from Hit as hit " +
           "where hit.requestTimestamp between ?1 and ?2 " +
           "group by hit.app, hit.uri " +
           "order by count(distinct hit.ip) desc")
    List<ViewStatsDto> findUniqueViewStatsBetweenStartEnd(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.evm.dto.ViewStatsDto(hit.app, hit.uri, count(hit.ip)) " +
           "from Hit as hit " +
           "where hit.requestTimestamp between ?1 and ?2 " +
           "group by hit.app, hit.uri " +
           "order by count(hit.ip) desc")
    List<ViewStatsDto> findNotUniqueViewStatsBetweenStartEnd(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.evm.dto.ViewStatsDto(hit.app, hit.uri, count(distinct hit.ip)) " +
           "from Hit as hit " +
           "where hit.requestTimestamp between ?1 and ?2 and hit.uri IN ?3 " +
           "group by hit.app, hit.uri " +
           "order by count(distinct hit.ip) desc")
    List<ViewStatsDto> findUniqueViewStatsBetweenStartEndWithFilterUri(LocalDateTime start, LocalDateTime end,
                                                                       List<String> filterUri);

    @Query("select new ru.practicum.evm.dto.ViewStatsDto(hit.app, hit.uri, count(hit.ip)) " +
           "from Hit as hit " +
           "where hit.requestTimestamp between ?1 and ?2 and hit.uri IN ?3 " +
           "group by hit.app, hit.uri " +
           "order by count(hit.ip) desc")
    List<ViewStatsDto> findNotUniqueViewStatsBetweenStartEndWithFilterUri(LocalDateTime start, LocalDateTime end,
                                                                          List<String> filterUri);
}
