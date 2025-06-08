package ru.practicum.evm.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.evm.dto.EndpointHitDto;
import ru.practicum.evm.dto.ViewStatsDto;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class StatClientImpl implements StatClient {
    private final RestClient restClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatClientImpl(@Value("http://localhost:9090") String serverUrl) {
        restClient = RestClient.builder()
                .baseUrl(serverUrl)
                .build();
    }

    @Override
    public void postHit(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(app);
        endpointHitDto.setUri(uri);
        endpointHitDto.setIp(ip);
        endpointHitDto.setTimestamp(LocalDateTime.now()
                .format(formatter));

        ResponseEntity<Void> response = restClient.post()
                .uri(UriBuilder ->
                        UriBuilder
                                .path("/hit")
                                .build())
                .body(endpointHitDto)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(code -> code.is4xxClientError() || code.is5xxServerError(),
                        (req, resp) -> {
                            throw new ConnectException("Connection refused");
                        })
                .toBodilessEntity();
    }

    @Override
    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        List<ViewStatsDto> response = restClient.get()
                .uri(UriBuilder -> {
                    UriBuilder.path("/stats")
                            .queryParam("start", start.format(formatter))
                            .queryParam("end", end.format(formatter));
                    if (uris != null && !uris.isEmpty()) {
                        UriBuilder.queryParam("uris", uris);
                    }
                    if (unique != null) {
                        UriBuilder.queryParam("unique", unique);
                    }
                    return UriBuilder.build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(code -> code.is4xxClientError() || code.is5xxServerError(),
                        (req, resp) -> {
                            throw new ConnectException("Connection refused");
                        })
                .body(new ParameterizedTypeReference<>() {
                });

        assert response != null;
        if (response.isEmpty()) {
            return new ArrayList<>();
        }
        return response;
    }
}
