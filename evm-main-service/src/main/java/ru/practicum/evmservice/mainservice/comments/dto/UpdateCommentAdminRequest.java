package ru.practicum.evmservice.mainservice.comments.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCommentAdminRequest {
    private String reason;

    private AdminCommentStateAction commentStateAction;
}
