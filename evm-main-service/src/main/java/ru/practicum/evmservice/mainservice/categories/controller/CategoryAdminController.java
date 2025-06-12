package ru.practicum.evmservice.mainservice.categories.controller;

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
import ru.practicum.evmservice.mainservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.mainservice.categories.dto.NewCategoryDto;
import ru.practicum.evmservice.mainservice.categories.service.CategoryService;

/**
 * API для работы с категориями
 */
@Slf4j
@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    /**
     * Добавление новой категории.
     * <p>
     * Имя категории должно быть уникальным.
     *
     * @param newCategoryDto данные добавляемой категории
     * @return данные добавленной категории в формате CategoryDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Create new category: {}", newCategoryDto);
        return categoryService.createCategory(newCategoryDto);
    }

    /**
     * Удаление категории.
     * <p>
     * С категорией не должно быть связано ни одного события.
     *
     * @param catId id категории
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        log.info("Delete category: {}", catId);
        categoryService.deleteCategory(catId);
    }

    /**
     * Изменение категории.
     * <p>
     * Имя категории должно быть уникальным.
     *
     * @param catId          id категории
     * @param newCategoryDto Данные категории для изменения
     * @return данные измененной категории в формате CategoryDto
     */
    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable int catId,
                                      @Valid @RequestBody CategoryDto newCategoryDto) {
        newCategoryDto.setId(catId);
        log.info("Update category: {}", newCategoryDto);
        return categoryService.updateCategory(newCategoryDto);
    }
}
