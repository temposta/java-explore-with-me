package ru.practicum.evmservice.mainservice.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.evmservice.mainservice.events.dto.EventFullDto;
import ru.practicum.evmservice.mainservice.events.dto.EventShortDto;
import ru.practicum.evmservice.mainservice.events.dto.NewEventDto;
import ru.practicum.evmservice.mainservice.events.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "createdOn", source = "createdDate")
    @Mapping(target = "publishedOn", source = "publishedDate")
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    EventFullDto toFullDto(Event event);

    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "initiator.id", source = "initiatorId")
    @Mapping(target = "location.lat", expression = "java(locationDto.getLat())")
    @Mapping(target = "location.lon", expression = "java(locationDto.getLon())")
    Event toEvent(NewEventDto newEventDto);

    EventShortDto toShortDto(Event event);

    List<EventShortDto> toShortDtos(List<Event> events);

    List<EventFullDto> toFullDtos(List<Event> events);

}
