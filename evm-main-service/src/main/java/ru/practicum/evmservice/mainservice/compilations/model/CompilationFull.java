package ru.practicum.evmservice.mainservice.compilations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.evmservice.mainservice.events.model.Event;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "compilations")
public class CompilationFull {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "pinned", nullable = false)
    private Boolean pinned = false;

    @Size(max = 50)
    @NotNull
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @ManyToMany
    @JoinTable(name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events = new HashSet<>();

}