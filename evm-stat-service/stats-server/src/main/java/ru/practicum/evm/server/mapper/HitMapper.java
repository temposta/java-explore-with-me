package ru.practicum.evm.server.mapper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.evm.dto.EndpointHitDto;
import ru.practicum.evm.server.model.Hit;

import java.time.LocalDateTime;

import static ru.practicum.evm.server.util.Constants.FORMATTER;

@Component
@NoArgsConstructor
public class HitMapper {

    public Hit fromDto (EndpointHitDto endpointHitDto) {

        Hit hit = new Hit();
        hit.setApp(endpointHitDto.getApp());
        hit.setIp(endpointHitDto.getIp());
        hit.setUri(endpointHitDto.getUri());
        hit.setRequestTimestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), FORMATTER));
        return hit;
    }

    public EndpointHitDto toDto (Hit hit) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setId(hit.getId());
        endpointHitDto.setApp(hit.getApp());
        endpointHitDto.setIp(hit.getIp());
        endpointHitDto.setUri(hit.getUri());
        endpointHitDto.setTimestamp(hit.getRequestTimestamp().format(FORMATTER));
        return endpointHitDto;
    }
}
