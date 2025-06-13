package ru.practicum.evmservice.mainservice.comments.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.mainservice.comments.dto.AdminCommentStateAction;
import ru.practicum.evmservice.mainservice.comments.dto.CommentFullDto;
import ru.practicum.evmservice.mainservice.comments.dto.CommentShortDto;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsCriteria;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsUserCriteria;
import ru.practicum.evmservice.mainservice.comments.dto.NewCommentDto;
import ru.practicum.evmservice.mainservice.comments.dto.UpdateCommentAdminRequest;
import ru.practicum.evmservice.mainservice.comments.dto.UpdateCommentUserRequest;
import ru.practicum.evmservice.mainservice.comments.dto.UserCommentStateAction;
import ru.practicum.evmservice.mainservice.comments.mapper.CommentMapper;
import ru.practicum.evmservice.mainservice.comments.model.Comment;
import ru.practicum.evmservice.mainservice.comments.model.CommentState;
import ru.practicum.evmservice.mainservice.comments.repository.CommentRepository;
import ru.practicum.evmservice.mainservice.comments.repository.CustomCommentJpaApiRepository;
import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.events.model.Event;
import ru.practicum.evmservice.mainservice.events.repository.EventRepository;
import ru.practicum.evmservice.mainservice.exceptions.IncorrectRequestException;
import ru.practicum.evmservice.mainservice.exceptions.NotFoundException;
import ru.practicum.evmservice.mainservice.users.model.User;
import ru.practicum.evmservice.mainservice.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CustomCommentJpaApiRepository customCommentJpaApiRepository;

    @Override
    public List<CommentShortDto> getUserComments(int userId, int from, int size) {
        checkUser(userId);
        List<Comment> comments = commentRepository.findUserComments(userId, from, size);
        return CommentMapper.INSTANCE.toCommentShortDtoList(comments);
    }

    @Override
    public CommentFullDto createComment(Integer userId, NewCommentDto newCommentDto) {
        User user = checkUser(userId);
        Event event = eventRepository.findById(newCommentDto.getEvent())
                .orElseThrow(() -> new NotFoundException("Event with id=" + newCommentDto.getEvent() + " was not found"));
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new IncorrectRequestException("Event with id=" + newCommentDto.getEvent() + " is not published");
        }
        Comment comment = Comment.builder()
                .event(event)
                .comment(newCommentDto.getComment())
                .status(CommentState.PENDING)
                .createdDate(LocalDateTime.now())
                .user(user)
                .build();
        comment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.toCommentFullDto(comment);
    }

    @Override
    public CommentFullDto getComment(Integer userId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new IncorrectRequestException("Comment with id=" + commentId + " is not author of user id=" + comment.getUser().getId());
        }
        return CommentMapper.INSTANCE.toCommentFullDto(comment);
    }

    @Override
    public CommentFullDto patchComment(Integer userId, Integer commentId, UpdateCommentUserRequest updateCommentUserRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new IncorrectRequestException("Comment with id=" + commentId + " is not author of user id=" + comment.getUser().getId());
        }
        if (updateCommentUserRequest.getComment() != null) {
            comment.setComment(updateCommentUserRequest.getComment());
        }
        UserCommentStateAction action = updateCommentUserRequest.getStateAction();
        if (action != null) {
            if (CommentState.PUBLISHED.equals(comment.getStatus())) {
                throw new IncorrectRequestException("Comment with id=" + commentId + " is already published");
            }
            switch (action) {
                case SEND_TO_REVIEW -> comment.setStatus(CommentState.PENDING);
                case CANCEL_REVIEW -> comment.setStatus(CommentState.DELETED);
            }
        }
        comment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.toCommentFullDto(comment);
    }

    @Override
    public List<CommentFullDto> getAdminComments(FindCommentsCriteria criteria) {
        List<Comment> comments = customCommentJpaApiRepository.findAllCommentsByCriteria(criteria);
        return CommentMapper.INSTANCE.toCommentFullDtoList(comments);
    }

    @Override
    public CommentFullDto updateCommentFromAdmin(int commentId, UpdateCommentAdminRequest updateCommentAdminRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        AdminCommentStateAction action = updateCommentAdminRequest.getCommentStateAction();
        if (action != null) {
            if (CommentState.PUBLISHED.equals(comment.getStatus())) {
                throw new IncorrectRequestException("Comment with id=" + commentId + " is already published");
            }
            switch (action) {
                case PUBLISH_COMMENT -> {
                    comment.setStatus(CommentState.PUBLISHED);
                    comment.setPublishedDate(LocalDateTime.now());
                }
                case REJECT_COMMENT -> comment.setStatus(CommentState.REJECTED);
            }
        }
        if (updateCommentAdminRequest.getReason() != null) {
            comment.setReason(updateCommentAdminRequest.getReason());
        }
        comment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.toCommentFullDto(comment);
    }

    @Override
    public List<CommentShortDto> findAllCommentsByUserCriteria(FindCommentsUserCriteria criteria) {
        List<Comment> comments = customCommentJpaApiRepository.findAllCommentsByUserCriteria(criteria);
        return CommentMapper.INSTANCE.toCommentShortDtoList(comments);
    }

    @Override
    public CommentFullDto getPublicComment(int commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        if(!CommentState.PUBLISHED.equals(comment.getStatus())) {
            throw new IncorrectRequestException("Comment with id=" + commentId + " is not published");
        }
        return CommentMapper.INSTANCE.toCommentFullDto(comment);
    }

    private User checkUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }
}
