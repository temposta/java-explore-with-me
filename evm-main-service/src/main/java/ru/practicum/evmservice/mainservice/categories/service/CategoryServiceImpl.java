package ru.practicum.evmservice.mainservice.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.mainservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.mainservice.categories.dto.NewCategoryDto;
import ru.practicum.evmservice.mainservice.categories.mapper.CategoryMapper;
import ru.practicum.evmservice.mainservice.categories.model.Category;
import ru.practicum.evmservice.mainservice.categories.repository.CategoryRepository;
import ru.practicum.evmservice.mainservice.exceptions.NotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository
                .save(CategoryMapper.INSTANCE.newCategoryDtoToCategory(newCategoryDto));
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }

    @Override
    public void deleteCategory(int catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto newCategoryDto) {
        categoryRepository.findById(newCategoryDto.getId())
                .orElseThrow(() -> new NotFoundException("Category with id=" + newCategoryDto.getId() +
                                                         " was not found"));
        Category category = categoryRepository.save(CategoryMapper.INSTANCE.CategoryDtoToCategory(newCategoryDto));
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        List<Category> categories = categoryRepository.findAllWithOffsetAndSize(from, size);
        return CategoryMapper.INSTANCE.categoryListToCategoryDtoList(categories);
    }

    @Override
    public CategoryDto getCategory(int catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }
}
