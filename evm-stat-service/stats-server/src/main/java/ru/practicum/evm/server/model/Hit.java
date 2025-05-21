package ru.practicum.evm.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "app", nullable = false, length = Integer.MAX_VALUE)
    private String app;

    @NotNull
    @Column(name = "uri", nullable = false, length = Integer.MAX_VALUE)
    private String uri;

    @Size(max = 15)
    @NotNull
    @Column(name = "ip", nullable = false, length = 15)
    private String ip;

    @NotNull
    @Column(name = "request_timestamp", nullable = false)
    private LocalDateTime requestTimestamp;

}