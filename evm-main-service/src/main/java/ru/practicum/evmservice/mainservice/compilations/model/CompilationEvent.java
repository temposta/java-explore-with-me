package ru.practicum.evmservice.mainservice.compilations.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "compilation_events")
public class CompilationEvent {
    @EmbeddedId
    private CompilationEventId id;

}