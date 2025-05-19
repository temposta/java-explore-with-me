package ru.practicum.evm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для сохранения информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
 * Название сервиса, uri и ip пользователя указаны в теле запроса.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {

    private Long id; //Идентификатор записи

    @NotNull
    private String app; //Идентификатор сервиса для которого записывается информация

    @NotNull
    private String uri; //URI для которого был осуществлен запрос

    @NotNull
    private String ip; //IP-адрес пользователя, осуществившего запрос

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",
            message = "Формат даты должен соответствовать шаблону - \"yyyy-MM-dd HH:mm:ss\"")
    private String timestamp; //Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
}