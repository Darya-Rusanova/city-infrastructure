package ru.isu.cityinfra.frontend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.isu.cityinfra.frontend.security.AuthInterceptor;
import ru.isu.cityinfra.frontend.security.NavInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NavInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/favicon.ico", "/error");

        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/register", "/css/**", "/js/**", "/favicon.ico", "/error");
    }
}
