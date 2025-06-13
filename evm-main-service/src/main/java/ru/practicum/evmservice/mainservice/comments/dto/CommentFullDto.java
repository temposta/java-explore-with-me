package ru.practicum.evmservice.mainservice.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.evmservice.mainservice.comments.model.CommentState;
import ru.practicum.evmservice.mainservice.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentFullDto {
    private String comment;
    private Integer eventId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private CommentState state;
    private UserShortDto author;
    private String reason;
}

