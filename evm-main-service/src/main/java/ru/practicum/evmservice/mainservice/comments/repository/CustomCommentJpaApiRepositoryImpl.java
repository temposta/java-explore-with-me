package ru.practicum.evmservice.mainservice.comments.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.evmservice.mainservice.comments.dto.CommentSort;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsCriteria;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsUserCriteria;
import ru.practicum.evmservice.mainservice.comments.model.Comment;
import ru.practicum.evmservice.mainservice.comments.model.CommentState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CustomCommentJpaApiRepositoryImpl implements CustomCommentJpaApiRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List findAllCommentsByCriteria(FindCommentsCriteria criteria) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = builder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        List<Integer> userIds = criteria.getUsers();
        if (userIds != null) {
            List<Predicate> predicatesForUsers = new ArrayList<>();
            for (Integer userId : userIds) {
                predicatesForUsers.add(builder.equal(root.get("user").get("id"), userId));
            }
            predicates.add(builder.or(predicatesForUsers.toArray(new Predicate[0])));
        }
        List<CommentState> states = criteria.getStates();
        if (states != null) {
            List<Predicate> predicatesForStates = new ArrayList<>();
            for (CommentState state : states) {
                predicatesForStates.add(builder.equal(root.get("status"), state));
            }
            predicates.add(builder.or(predicatesForStates.toArray(new Predicate[0])));
        }
        List<Integer> categories = criteria.getCategories();
        if (categories != null) {
            List<Predicate> predicatesForCategories = new ArrayList<>();
            for (Integer categoryId : categories) {
                predicatesForCategories.add(builder.equal(root.get("event").get("category").get("id"), categoryId));
            }
            predicates.add(builder.or(predicatesForCategories.toArray(new Predicate[0])));
        }
        LocalDateTime rangeStart = criteria.getRangeStart();
        LocalDateTime rangeEnd = criteria.getRangeEnd();
        if (rangeStart != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), rangeStart));
        }
        if (rangeEnd != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), rangeEnd));
        }
        Query query = em.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[0])));
        query.setMaxResults(criteria.getSize());
        query.setFirstResult(criteria.getFrom());

        var criteriaQueryResult = query.getResultList();
        return Objects.requireNonNullElseGet(criteriaQueryResult, List::of);

    }

    @Override
    public List findAllCommentsByUserCriteria(FindCommentsUserCriteria criteria) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = builder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        String text = criteria.getText();
        if (text != null) {
            predicates.add(builder.like(builder.lower(root.get("comment")), '%' + text.toLowerCase() + '%'));
        }
        List<Integer> categories = criteria.getCategories();
        if (categories != null) {
            List<Predicate> predicatesForCategories = new ArrayList<>();
            for (Integer categoryId : categories) {
                predicatesForCategories.add(builder.equal(root.get("event").get("category").get("id"), categoryId));
            }
            predicates.add(builder.or(predicatesForCategories.toArray(new Predicate[0])));
        }
        LocalDateTime rangeStart = criteria.getRangeStart();
        LocalDateTime rangeEnd = criteria.getRangeEnd();
        if (rangeStart != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), rangeStart));
        }
        if (rangeEnd != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), rangeEnd));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        CommentSort sort = criteria.getSort();
        if (sort != null) {
            switch (sort) {
                case ASC -> criteriaQuery.orderBy(builder.asc(root.get("createdDate")));
                case DESC -> criteriaQuery.orderBy(builder.desc(root.get("createdDate")));
            }
        }

        Query query = em.createQuery(criteriaQuery);
        query.setMaxResults(criteria.getSize());
        query.setFirstResult(criteria.getFrom());

        var criteriaQueryResult = query.getResultList();
        return Objects.requireNonNullElseGet(criteriaQueryResult, List::of);
    }
}
