package ru.practicum.evmservice.mainservice.categories.service;

import ru.practicum.evmservice.mainservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.mainservice.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(int catId);

    CategoryDto updateCategory(CategoryDto newCategoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(int catId);
}
