package ru.isu.cityinfra.frontend.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.frontend.dto.AuthTokenDto;
import ru.isu.cityinfra.frontend.dto.LoginRequestDto;
import ru.isu.cityinfra.frontend.dto.RegisterRequestDto;
import ru.isu.cityinfra.frontend.dto.UserDto;
import ru.isu.cityinfra.frontend.dto.UserPublicDto;

@Component
public class AuthClient {

    private final ApiGateway api;
    private final String baseUrl;

    public AuthClient(ApiGateway api, @Value("${services.auth.url}") String baseUrl) {
        this.api = api;
        this.baseUrl = baseUrl;
    }

    public AuthTokenDto login(LoginRequestDto request) {
        return api.post(ApiGateway.uri(baseUrl, "/auth/login"), null, request, AuthTokenDto.class);
    }

    public UserDto register(RegisterRequestDto request) {
        return api.post(ApiGateway.uri(baseUrl, "/auth/register"), null, request, UserDto.class);
    }

    public UserDto me(String token) {
        return api.get(ApiGateway.uri(baseUrl, "/users/me"), token, UserDto.class);
    }

    public List<UserPublicDto> users(String token) {
        return api.getList(ApiGateway.uri(baseUrl, "/users"), token, UserPublicDto[].class);
    }
}
