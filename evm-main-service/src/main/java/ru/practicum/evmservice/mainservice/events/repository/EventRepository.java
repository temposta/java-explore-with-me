package ru.practicum.evmservice.mainservice.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>{

    @Query("select e from Event as e where e.initiator.id = :userId order by e.id limit :limit offset :offset ")
    List<Event> findAllByInitiator_IdWithOffsetAndLimit(int userId, int offset, int limit);

    Optional<Event> findEventByIdAndState(Integer id, EventState state);

    @Modifying
    @Query("update Event as e set e.confirmedRequests = e.confirmedRequests + :count where e.id = :eventId")
    void incrementConfirmedRequests(int eventId, int count);

    @Modifying
    @Query("update Event as e set e.confirmedRequests = e.confirmedRequests - 1 where e.id = :eventId")
    void decrementConfirmedRequests(int eventId);

    @Query("select e from Event as e where (e.annotation ilike :text or e.description ilike :text) and " +
           "e.category.id in (:categories) and e.paid = :paid and e.eventDate between :rangeStart and :rangeEnd " +
           "order by e.eventDate " +
           "limit :size offset :from ")
    List<Event> getEventsWithFiltersOrderByEventDate(String text, List<Integer> categories, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     int from, int size);

    @Query("select e from Event as e where (e.annotation ilike :text or e.description ilike :text) and " +
           "e.category.id in (:categories) and e.paid = :paid and e.eventDate between :rangeStart and :rangeEnd " +
           "and (e.participantLimit = 0 or e.participantLimit-e.confirmedRequests > 0)" +
           "order by e.eventDate " +
           "limit :size offset :from ")
    List<Event> getEventsWithFiltersOnlyAvailableOrderByEventDate(String text, List<Integer> categories, Boolean paid,
                                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                  int from, int size);

    @Query("select e from Event as e where (e.annotation ilike :text or e.description ilike :text) and " +
           "e.paid = :paid and e.eventDate between :rangeStart and :rangeEnd " +
           "and (e.participantLimit = 0 or e.participantLimit-e.confirmedRequests > 0)" +
           "order by e.eventDate " +
           "limit :size offset :from ")
    List<Event> getEventsWithFiltersOnlyAvailableOrderByEventDate(String text, Boolean paid,
                                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                  int from, int size);

    @Query("select e from Event as e where (e.annotation ilike :text or e.description ilike :text) and " +
           "e.eventDate between :rangeStart and :rangeEnd " +
           "and (e.participantLimit = 0 or e.participantLimit-e.confirmedRequests > 0)" +
           "order by e.eventDate " +
           "limit :size offset :from ")
    List<Event> getEventsWithFiltersOnlyAvailableOrderByEventDate(String text, LocalDateTime rangeStart,
                                                                  LocalDateTime rangeEnd, int from, int size);

    @Query("select e from Event as e where (e.annotation ilike :text or e.description ilike :text) and " +
           "e.category.id in (:categories) and e.eventDate between :rangeStart and :rangeEnd " +
           "and (e.participantLimit = 0 or e.participantLimit-e.confirmedRequests > 0)" +
           "order by e.eventDate " +
           "limit :size offset :from ")
    List<Event> getEventsWithFiltersOnlyAvailableOrderByEventDate(String text, List<Integer> categories,
                                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                  int from, int size);

    @Query("select e from Event as e where (e.annotation ilike :text or e.description ilike :text) and " +
           "e.eventDate between :rangeStart and :rangeEnd " +
           "order by e.eventDate " +
           "limit :size offset :from ")
    List<Event> getEventsWithFiltersOrderByEventDate(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     int from, int size);

    @Query("select e from Event as e where (e.annotation ilike :text or e.description ilike :text) and " +
           "e.paid = :paid and e.eventDate between :rangeStart and :rangeEnd " +
           "order by e.eventDate " +
           "limit :size offset :from ")
    List<Event> getEventsWithFiltersOrderByEventDate(String text, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     int from, int size);

    @Query("select e from Event as e where (e.annotation ilike :text or e.description ilike :text) and " +
           "e.category.id in (:categories) and e.eventDate between :rangeStart and :rangeEnd " +
           "order by e.eventDate " +
           "limit :size offset :from ")
    List<Event> getEventsWithFiltersOrderByEventDate(String text, List<Integer> categories,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     int from, int size);
}
