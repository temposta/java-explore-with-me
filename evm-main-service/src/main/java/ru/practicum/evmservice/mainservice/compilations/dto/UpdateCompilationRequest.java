package ru.practicum.evmservice.mainservice.compilations.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UpdateCompilationRequest {

    private final List<Integer> events;

    private final Boolean pinned;

    @Size(min = 1, max = 50)
    private final String title;

}
