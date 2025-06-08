package ru.practicum.evmservice.mainservice.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.evmservice.mainservice.compilations.model.CompilationEvent;
import ru.practicum.evmservice.mainservice.compilations.model.CompilationEventId;

public interface CompilationEventsRepository extends JpaRepository<CompilationEvent, CompilationEventId> {

    @Modifying
    @Query("delete from CompilationEvent as ce where ce.id.compilationId = :compId")
    void deleteCompilationEventsById(Integer compId);
}
