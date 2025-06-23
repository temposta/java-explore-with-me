package ru.practicum.evmservice.mainservice.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.comments.dto.CommentFullDto;
import ru.practicum.evmservice.mainservice.comments.dto.CommentShortDto;
import ru.practicum.evmservice.mainservice.comments.dto.CommentSort;
import ru.practicum.evmservice.mainservice.comments.dto.FindCommentsUserCriteria;
import ru.practicum.evmservice.mainservice.comments.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Публичный API для работы с комментариями
 */
@Slf4j
@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentShortDto> getPublicComments(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Integer> categories,
                                           @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @RequestParam(required = false) CommentSort sort,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("getPublicComments called");
        FindCommentsUserCriteria criteria = FindCommentsUserCriteria.builder()
                .from(from)
                .size(size)
                .build();
        FindCommentsUserCriteria.FindCommentsUserCriteriaBuilder builder = criteria.toBuilder();
        if (text != null) {
            builder.text(text);
        }
        if (categories != null) {
            builder.categories(categories);
        }
        if (rangeStart != null) {
            builder.rangeStart(rangeStart);
        }
        if (rangeEnd != null) {
            builder.rangeEnd(rangeEnd);
        }
        if (sort != null) {
            builder.sort(sort);
        }
        criteria = builder.build();
        return commentService.findAllCommentsByUserCriteria(criteria);
    }

    @GetMapping("/{commentId}")
    public CommentFullDto getPublicComment(@PathVariable int commentId) {
        log.info("getPublicComment called");
        return commentService.getPublicComment(commentId);
    }

}
