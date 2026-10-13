package ru.isu.cityinfra.frontend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.frontend.dto.IssueRequestDto;
import ru.isu.cityinfra.frontend.dto.IssueResponseDto;
import ru.isu.cityinfra.frontend.dto.IssueStatusUpdateDto;

import java.util.List;

@Component
public class UtilityClient {

    private final ApiGateway api;
    private final String baseUrl;

    public UtilityClient(ApiGateway api, @Value("${services.utility.url}") String baseUrl) {
        this.api = api;
        this.baseUrl = baseUrl;
    }

    /** Пустые параметры пропускаются. USER получает только свои заявки, ADMIN все. */
    public List<IssueResponseDto> getIssues(String token, String status, String category,
                                            String sortBy, String dir) {
        return api.getList(ApiGateway.uri(baseUrl, "/issues",
                        "status", status, "type", category, "sortBy", sortBy, "dir", dir),
                token, IssueResponseDto[].class);
    }

    public IssueResponseDto createIssue(String token, IssueRequestDto request) {
        return api.post(ApiGateway.uri(baseUrl, "/issues"), token, request, IssueResponseDto.class);
    }

    public IssueResponseDto updateStatus(String token, Integer issueId, IssueStatusUpdateDto request) {
        return api.put(ApiGateway.uri(baseUrl, "/issues/" + issueId), token, request, IssueResponseDto.class);
    }
}
