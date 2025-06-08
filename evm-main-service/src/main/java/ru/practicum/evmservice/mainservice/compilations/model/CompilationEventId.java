package ru.practicum.evmservice.mainservice.compilations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CompilationEventId implements Serializable {
    @Serial
    private static final long serialVersionUID = -3922579778271650606L;
    @NotNull
    @Column(name = "compilation_id", nullable = false)
    private Integer compilationId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Integer eventId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CompilationEventId entity = (CompilationEventId) o;
        return Objects.equals(this.compilationId, entity.compilationId) &&
               Objects.equals(this.eventId, entity.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compilationId, eventId);
    }

}