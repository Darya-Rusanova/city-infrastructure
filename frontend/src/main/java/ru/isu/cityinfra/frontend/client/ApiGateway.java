package ru.isu.cityinfra.frontend.client;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.isu.cityinfra.frontend.exception.ApiException;

/**
 * Единая точка вызовов бэкенд-сервисов: добавляет токен и превращает ошибки в ApiException.
 */
@Slf4j
@Component
public class ApiGateway {

    private final RestTemplate restTemplate;

    public ApiGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** Собирает адрес запроса. Параметры передаются парами "имя, значение", пустые значения пропускаются. */
    public static URI uri(String base, String path, String... params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base + path);
        for (int i = 0; i + 1 < params.length; i += 2) {
            String value = params[i + 1];
            if (value != null && !value.isBlank()) {
                builder.queryParam(params[i], value);
            }
        }
        return builder.build().encode().toUri();
    }

    public <T> T get(URI uri, String token, Class<T> type) {
        return call(HttpMethod.GET, uri, token, null, null, type);
    }

    public <T> List<T> getList(URI uri, String token, Class<T[]> arrayType) {
        T[] body = call(HttpMethod.GET, uri, token, null, null, arrayType);
        return body == null ? List.of() : Arrays.asList(body);
    }

    public <T> T post(URI uri, String token, Object body, Class<T> type) {
        return call(HttpMethod.POST, uri, token, body, null, type);
    }

    public <T> T post(URI uri, String token, Object body, Map<String, String> headers, Class<T> type) {
        return call(HttpMethod.POST, uri, token, body, headers, type);
    }

    public <T> T put(URI uri, String token, Object body, Class<T> type) {
        return call(HttpMethod.PUT, uri, token, body, null, type);
    }

    private <T> T call(HttpMethod method, URI uri, String token, Object body,
                       Map<String, String> extraHeaders, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (token != null) {
            headers.setBearerAuth(token);
        }
        if (body != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        if (extraHeaders != null) {
            extraHeaders.forEach(headers::set);
        }
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<T> response = restTemplate.exchange(uri, method, entity, type);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            int status = e.getStatusCode().value();
            log.warn("Сервис ответил {} на {} {}", status, method, uri.getPath());
            throw new ApiException(status, ApiErrors.toMessage(status, e.getResponseBodyAsString()));
        } catch (RestClientException e) {
            log.error("Сервис недоступен: {} {}: {}", method, uri, e.getMessage());
            throw new ApiException(503, "Сервис временно недоступен, попробуйте позже");
        }
    }
}
