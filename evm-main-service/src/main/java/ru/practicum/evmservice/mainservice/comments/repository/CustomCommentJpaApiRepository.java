package ru.practicum.evmservice.mainservice.comments.repository;

import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsCriteria;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsUserCriteria;
import ru.practicum.evmservice.mainservice.comments.model.Comment;

import java.util.List;

public interface CustomCommentJpaApiRepository {

    List<Comment> findAllCommentsByCriteria(FindCommentsCriteria criteria);

    List<Comment> findAllCommentsByUserCriteria(FindCommentsUserCriteria criteria);
}
