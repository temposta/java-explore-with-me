package ru.practicum.evmservice.mainservice.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.evmservice.mainservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.mainservice.requests.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "created", source = "createdDate")
    @Mapping(target = "event", source = "eventId")
    @Mapping(target = "requester", source = "requesterId")
    ParticipationRequestDto toParticipationDto(Request request);

    List<ParticipationRequestDto> toParticipationDtoList(List<Request> requests);


//    @Mapping(target = "createdOn", source = "createdDate")
//    EventFullDto toFullDto(Event event);
//
//    @Mapping(target = "category.id", source = "category")
//    @Mapping(target = "initiator.id", source = "initiatorId")
//    @Mapping(target = "location.lat", expression = "java(locationDto.getLat())")
//    @Mapping(target = "location.lon", expression = "java(locationDto.getLon())")
//    Event toEvent(NewEventDto newEventDto);
//
//    EventShortDto toShortDto(Event event);
//
//    List<EventShortDto> toShortDtos(List<Event> events);
//
//    List<EventFullDto> toFullDtos(List<Event> events);

}
