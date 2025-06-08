package ru.practicum.evmservice.mainservice.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class NewCompilationDto {

    private final List<Integer> events = new ArrayList<>();

    private final Boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private final String title;
}
