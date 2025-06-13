package ru.practicum.evmservice.mainservice.comments.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.practicum.evmservice.mainservice.comments.model.CommentState;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindCommentsCriteria {
    List<Integer> users;
    List<CommentState> states;
    List<Integer> categories;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    int from;
    int size;
}
