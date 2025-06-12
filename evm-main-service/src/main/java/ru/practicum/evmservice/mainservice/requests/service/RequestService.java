package ru.practicum.evmservice.mainservice.requests.service;

import ru.practicum.evmservice.mainservice.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.evmservice.mainservice.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.evmservice.mainservice.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequests(int userId);

    ParticipationRequestDto createRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);

    List<ParticipationRequestDto> getRequestsOnEvent(int userId, Integer eventId);

    EventRequestStatusUpdateResult updateRequests(int userId, Integer eventId,
                                                        EventRequestStatusUpdateRequest
                                                                eventRequestStatusUpdateRequest);
}
