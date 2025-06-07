package ru.practicum.evmservice.mainservice.requests.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;

    private Status status;

    public enum Status {
        CONFIRMED, REJECTED
    }
}
