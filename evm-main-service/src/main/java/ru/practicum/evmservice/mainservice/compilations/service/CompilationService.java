package ru.practicum.evmservice.mainservice.compilations.service;

import ru.practicum.evmservice.mainservice.compilations.dto.CompilationDto;
import ru.practicum.evmservice.mainservice.compilations.dto.NewCompilationDto;
import ru.practicum.evmservice.mainservice.compilations.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Integer compId);

    CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilationRequest);

    CompilationDto getCompilation(Integer compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
}
