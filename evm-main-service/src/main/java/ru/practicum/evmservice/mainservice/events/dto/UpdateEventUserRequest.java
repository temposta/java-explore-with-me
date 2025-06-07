package ru.practicum.evmservice.mainservice.events.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class UpdateEventUserRequest extends UpdateEventRequest {

    private StateAction stateAction;
}
