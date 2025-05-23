package ru.practicum.evmservice.mainservice.categories.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.evmservice.mainservice.categories.dto.CategoryDto;
import ru.practicum.evmservice.mainservice.categories.dto.NewCategoryDto;
import ru.practicum.evmservice.mainservice.categories.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto categoryToCategoryDto(Category category);

    Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto);

    Category CategoryDtoToCategory(CategoryDto categoryDto);

    List<CategoryDto> categoryListToCategoryDtoList(List<Category> categoryList);
}
