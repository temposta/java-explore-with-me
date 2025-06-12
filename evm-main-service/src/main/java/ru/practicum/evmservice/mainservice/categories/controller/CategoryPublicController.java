package ru.practicum.evmservice.mainservice.categories.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.mainservice.categories.service.CategoryService;

import java.util.List;

/**
 * Публичный API для работы с категориями
 */
@Slf4j
@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryPublicController {
    private CategoryService categoryService;

    /**
     * Получение категорий.
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список.
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     *             Default value : 0
     * @param size количество категорий в наборе
     *             Default value : 10
     * @return список категорий в формате CategoryDto
     */
    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("getCategories: from={}, size={}", from, size);
        return categoryService.getCategories(from, size);
    }

    /**
     * Получение информации о категории по её идентификатору.
     * <p>
     * В случае, если категории с заданным id не найдено, возвращает статус код 404.
     *
     * @param catId id категории
     * @return информация о категории в формате CategoryDto
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable int catId) {
        log.info("getCategory: catId={}", catId);
        return categoryService.getCategory(catId);
    }
}
