package ru.practicum.evmservice.mainservice.compilations.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.compilations.dto.CompilationDto;
import ru.practicum.evmservice.mainservice.compilations.dto.NewCompilationDto;
import ru.practicum.evmservice.mainservice.compilations.dto.UpdateCompilationRequest;
import ru.practicum.evmservice.mainservice.compilations.service.CompilationService;

/**
 * API для работы с подборками событий
 */
@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     * @param newCompilationDto данные новой подборки
     * @return данные добавленной подборки
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Creating new compilation: {}", newCompilationDto);
        return compilationService.createCompilation(newCompilationDto);
    }

    /**
     * Удаление подборки
     * @param compId id подборки
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        log.info("Deleting compilation: {}", compId);
        compilationService.deleteCompilation(compId);
    }

    /**
     * Обновить информацию о подборке
     * @param compId id подборки
     * @param updateCompilationRequest данные для обновления подборки
     * @return данные после обновления
     */
    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Integer compId,
                                            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Updating compilation: {}", updateCompilationRequest);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }

}
