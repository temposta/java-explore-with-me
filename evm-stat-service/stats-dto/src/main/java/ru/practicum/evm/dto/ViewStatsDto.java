package ru.practicum.evm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO ответа при запросе статистики по посещениям
 */
@Data
@NoArgsConstructor
public class ViewStatsDto {

    private String app; //Название сервиса

    private String uri; //URI сервиса

    private Long hits; //Количество просмотров

    public ViewStatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}