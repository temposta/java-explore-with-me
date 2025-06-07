package ru.practicum.evmservice.mainservice.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evmservice.mainservice.requests.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request,Integer> {

    List<Request> findAllByRequesterId(int id);

    List<Request> findAllByRequesterIdAndEventId(int id, int eventId);

    List<Request> findAllByEventId(int eventId);

    @Query("select r from Request as r where r.eventId = :eventId and r.id in (:ids) order by r.createdDate")
    List<Request> findAllByEventIdAndIdInOrderByCreatedDateAsc(int eventId, List<Integer> ids);

    Optional<Request> findByRequesterIdAndEventId(int id, int eventId);
}
