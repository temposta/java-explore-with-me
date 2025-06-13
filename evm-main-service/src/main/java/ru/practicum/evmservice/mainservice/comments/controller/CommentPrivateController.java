package ru.practicum.evmservice.mainservice.comments.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.comments.dto.CommentFullDto;
import ru.practicum.evmservice.mainservice.comments.dto.CommentShortDto;
import ru.practicum.evmservice.mainservice.comments.dto.NewCommentDto;
import ru.practicum.evmservice.mainservice.comments.dto.UpdateCommentUserRequest;
import ru.practicum.evmservice.mainservice.comments.service.CommentService;

import java.util.List;

/**
 * Закрытый API для работы с комментариями
 */
@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@AllArgsConstructor
public class CommentPrivateController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentShortDto> getUserComments(@PathVariable int userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("getUserComments userId = {}, from = {}, size = {}", userId, from, size);
        return commentService.getUserComments(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable Integer userId,
                                        @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("createComment userId = {}, newCommentDto = {}", userId, newCommentDto);
        return commentService.createComment(userId, newCommentDto);
    }

    @GetMapping("/{commentId}")
    public CommentFullDto getComment(@PathVariable Integer userId,
                                     @PathVariable Integer commentId) {
        log.info("getComment userId = {}, commentId = {}", userId, commentId);
        return commentService.getComment(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto patchComment(@PathVariable Integer userId,
                                       @PathVariable Integer commentId,
                                       @Valid @RequestBody UpdateCommentUserRequest updateCommentUserRequest) {
        log.info("patchComment userId = {}, commentId = {}, patch = {}", userId, commentId, updateCommentUserRequest);
        return commentService.patchComment(userId, commentId, updateCommentUserRequest);
    }

}
