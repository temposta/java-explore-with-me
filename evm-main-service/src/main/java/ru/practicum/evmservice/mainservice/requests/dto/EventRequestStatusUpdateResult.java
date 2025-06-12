package ru.practicum.evmservice.mainservice.requests.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;

    List<ParticipationRequestDto> rejectedRequests;
}
