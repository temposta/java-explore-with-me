package ru.practicum.evmservice.mainservice.events.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.events.model.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomEventRepositoryImpl implements CustomEventRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List findEventsByFilter(List<Integer> users, List<EventState> states, List<Integer> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (users != null) {
            List<Predicate> predicatesForUsers = new ArrayList<>();
            users.forEach(user -> predicatesForUsers.add(cb.equal(root.get("initiator").get("id"), user)));
            predicates.add(cb.or(predicatesForUsers.toArray(new Predicate[0])));
        }
        if (states != null) {
            List<Predicate> predicatesForStates = new ArrayList<>();
            states.forEach(state -> predicatesForStates.add(cb.equal(root.get("state"), state)));
            predicates.add(cb.or(predicatesForStates.toArray(new Predicate[0])));
        }
        if (categories != null) {
            List<Predicate> predicatesForCategories = new ArrayList<>();
            categories.forEach(category -> predicatesForCategories.add(cb.equal(root.get("category").get("id"), category)));
            predicates.add(cb.or(predicatesForCategories.toArray(new Predicate[0])));
        }
        if (rangeStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }
        if (rangeEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        Query query = em.createQuery(cq.where(predicates.toArray(new Predicate[0])));
        query.setMaxResults(size);
        query.setFirstResult(from);

        var events = query.getResultList();

        if (query.getResultList().isEmpty()) {
            return List.of();
        }
        return events;

    }
}
