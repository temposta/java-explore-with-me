package ru.practicum.evmservice.mainservice.comments.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindCommentsUserCriteria {
    String text;
    List<Integer> categories;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    CommentSort sort;
    int from;
    int size;
}
