package ru.practicum.evmservice.mainservice.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evmservice.mainservice.categories.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select c from Category as c order by c.id limit :size offset :offset")
    List<Category> findAllWithOffsetAndSize(int offset, int size);
}
