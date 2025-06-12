package ru.practicum.evmservice.mainservice.events.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {

    private StateAction stateAction;
}
