package ru.practicum.evmservice.mainservice.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.evmservice.mainservice.comments.dto.CommentFullDto;
import ru.practicum.evmservice.mainservice.comments.dto.CommentShortDto;
import ru.practicum.evmservice.mainservice.comments.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "createdOn", source = "createdDate")
    @Mapping(target = "publishedOn", source = "publishedDate")
    @Mapping(target = "state", source = "status")
    @Mapping(target = "author", source = "user")
    CommentFullDto toCommentFullDto(Comment comment);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "createdOn", source = "createdDate")
    @Mapping(target = "publishedOn", source = "publishedDate")
    @Mapping(target = "state", source = "status")
    CommentShortDto toCommentShortDto(Comment comment);

    List<CommentShortDto> toCommentShortDtoList(List<Comment> comments);

    List<CommentFullDto> toCommentFullDtoList(List<Comment> comments);
}
