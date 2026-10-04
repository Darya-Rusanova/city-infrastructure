package ru.isu.cityinfra.frontend.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.isu.cityinfra.frontend.dto.IssueRequestDto;
import ru.isu.cityinfra.frontend.dto.IssueResponseDto;
import ru.isu.cityinfra.frontend.dto.IssueStatusUpdateDto;

import java.util.List;

@Component
@Slf4j
public class UtilityClient {

    private final RestTemplate restTemplate;
    private final String utilityUrl;

    public UtilityClient(RestTemplate restTemplate,
                          @Value("${services.utility.url}") String utilityUrl) {
        this.restTemplate = restTemplate;
        this.utilityUrl = utilityUrl;
    }

    private HttpHeaders authHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    public List<IssueResponseDto> getAllIssues(String token) {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders(token));
        ResponseEntity<IssueResponseDto[]> response = restTemplate.exchange(
                utilityUrl + "/issues", HttpMethod.GET, entity, IssueResponseDto[].class);
        return response.getBody() != null ? List.of(response.getBody()) : List.of();
    }

    public IssueResponseDto createIssue(String token, IssueRequestDto request) {
        HttpEntity<IssueRequestDto> entity = new HttpEntity<>(request, authHeaders(token));
        ResponseEntity<IssueResponseDto> response = restTemplate.exchange(
                utilityUrl + "/issues", HttpMethod.POST, entity, IssueResponseDto.class);
        return response.getBody();
    }

    public IssueResponseDto updateStatus(String token, Integer issueId, IssueStatusUpdateDto request) {
        HttpEntity<IssueStatusUpdateDto> entity = new HttpEntity<>(request, authHeaders(token));
        ResponseEntity<IssueResponseDto> response = restTemplate.exchange(
                utilityUrl + "/issues/" + issueId, HttpMethod.PUT, entity, IssueResponseDto.class);
        return response.getBody();
    }
}
