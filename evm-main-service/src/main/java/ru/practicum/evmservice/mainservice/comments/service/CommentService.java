package ru.practicum.evmservice.mainservice.comments.service;

import ru.practicum.evmservice.mainservice.comments.dto.CommentFullDto;
import ru.practicum.evmservice.mainservice.comments.dto.CommentShortDto;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsCriteria;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsUserCriteria;
import ru.practicum.evmservice.mainservice.comments.dto.NewCommentDto;
import ru.practicum.evmservice.mainservice.comments.dto.UpdateCommentAdminRequest;
import ru.practicum.evmservice.mainservice.comments.dto.UpdateCommentUserRequest;

import java.util.List;

public interface CommentService {
    List<CommentShortDto> getUserComments(int userId, int from, int size);

    CommentFullDto createComment(Integer userId, NewCommentDto newCommentDto);

    CommentFullDto getComment(Integer userId, Integer commentId);

    CommentFullDto patchComment(Integer userId, Integer commentId, UpdateCommentUserRequest updateCommentUserRequest);

    List<CommentFullDto> getAdminComments(FindCommentsCriteria criteria);

    CommentFullDto updateCommentFromAdmin(int commentId, UpdateCommentAdminRequest updateCommentAdminRequest);

    List<CommentShortDto> findAllCommentsByUserCriteria(FindCommentsUserCriteria criteria);

    CommentFullDto getPublicComment(int commentId);
}
