package ru.practicum.evmservice.mainservice.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewCommentDto {
    @NotBlank
    @Size(min = 20, max = 7000)
    private String comment;
    private Integer event;
}
