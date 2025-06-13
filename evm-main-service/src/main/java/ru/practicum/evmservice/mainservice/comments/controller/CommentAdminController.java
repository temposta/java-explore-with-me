package ru.practicum.evmservice.mainservice.comments.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.comments.dto.CommentFullDto;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsCriteria;
import ru.practicum.evmservice.mainservice.comments.dto.UpdateCommentAdminRequest;
import ru.practicum.evmservice.mainservice.comments.model.CommentState;
import ru.practicum.evmservice.mainservice.comments.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * API для работы с комментариями
 */
@Slf4j
@RestController
@RequestMapping("/admin/comments")
@AllArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentFullDto> getAdminComments(@RequestParam(required = false) List<Integer> users,
                                               @RequestParam(required = false) List<CommentState> states,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        FindCommentsCriteria criteria = FindCommentsCriteria.builder()
                .from(from)
                .size(size)
                .build();
        FindCommentsCriteria.FindCommentsCriteriaBuilder criteriaBuilder = criteria.toBuilder();
        if (users != null) {
            criteriaBuilder.users(users);
        }
        if (states != null) {
            criteriaBuilder.states(states);
        }
        if (categories != null) {
            criteriaBuilder.categories(categories);
        }
        if (rangeStart != null) {
            criteriaBuilder.rangeStart(rangeStart);
        }
        if (rangeEnd != null) {
            criteriaBuilder.rangeEnd(rangeEnd);
        }
        criteria = criteriaBuilder.build();

        log.info("Find events criteria: {}", criteria);
        return commentService.getAdminComments(criteria);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto updateCommentFromAdmin(@PathVariable int commentId,
                                                 @RequestBody @Valid UpdateCommentAdminRequest updateCommentAdminRequest) {
        log.info("Update comment id={} from admin: {}", commentId, updateCommentAdminRequest);
        return commentService.updateCommentFromAdmin(commentId, updateCommentAdminRequest);
    }

}
