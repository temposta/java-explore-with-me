package ru.practicum.evmservice.mainservice.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.evmservice.mainservice.compilations.model.CompilationFull;

import java.util.List;

public interface FullCompilationRepository extends JpaRepository<CompilationFull, Integer> {

    List<CompilationFull> getCompilationsByPinned(Boolean pinned, Pageable pageable);
}
