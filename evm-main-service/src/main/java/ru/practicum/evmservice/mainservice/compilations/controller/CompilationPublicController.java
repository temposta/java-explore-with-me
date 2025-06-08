package ru.practicum.evmservice.mainservice.compilations.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.compilations.dto.CompilationDto;
import ru.practicum.evmservice.mainservice.compilations.service.CompilationService;

import java.util.List;

/**
 * Публичный API для работы с подборками событий
 */
@Slf4j
@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;

    /**
     * Получение подборки событий по его id
     *</p>
     * В случае, если подборки с заданным id не найдено, возвращает статус код 404
     * @param compId id подборки
     * @return Подборка событий
     */
    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Integer compId) {
        log.info("Getting compilation with id {}", compId);
        return compilationService.getCompilation(compId);
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("Getting compilations with pinned {}", pinned);
        return compilationService.getCompilations(pinned, from, size);
    }

}
