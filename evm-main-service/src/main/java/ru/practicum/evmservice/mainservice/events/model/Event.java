package ru.practicum.evmservice.mainservice.events.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.evmservice.mainservice.categories.model.Category;
import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.users.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 2000)
    @NotNull
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Size(max = 7000)
    @NotNull
    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Embedded
    @AttributeOverride(name = "lat",
            column = @Column(name = "lat"))
    @AttributeOverride(name = "lon",
            column = @Column(name = "lon"))
    private Location location;

    @ColumnDefault("false")
    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @ColumnDefault("0")
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @ColumnDefault("true")
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Size(max = 120)
    @NotNull
    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @ColumnDefault("(now() AT TIME ZONE 'utc')")
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 9)
    private EventState state;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        if (state == null) {
            state = EventState.PENDING;
        }
        if (confirmedRequests == null) {
            confirmedRequests = 0;
        }
        if (paid == null) {
            paid = false;
        }
        if (participantLimit == null) {
            participantLimit = 0;
        }
        if (requestModeration == null) {
            requestModeration = true;
        }
    }

}