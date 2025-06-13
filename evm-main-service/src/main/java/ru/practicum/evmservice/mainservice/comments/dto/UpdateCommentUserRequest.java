package ru.practicum.evmservice.mainservice.comments.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCommentUserRequest {
    @Nullable
    @Size(min = 20, max = 7000)
    private String comment;

    private UserCommentStateAction stateAction;

}
