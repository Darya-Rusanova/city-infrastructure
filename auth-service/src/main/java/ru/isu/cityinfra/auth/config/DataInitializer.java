package ru.isu.cityinfra.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.isu.cityinfra.auth.enums.Role;
import ru.isu.cityinfra.auth.model.User;
import ru.isu.cityinfra.auth.repository.UserRepository;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        userRepository.save(User.builder()
                .username("admin")
                .email("admin@city.local")
                .passwordHash(passwordEncoder.encode("admin123"))
                .fullName("Администратор Системы")
                .role(Role.ADMIN)
                .build());

        userRepository.save(User.builder()
                .username("user1")
                .email("user1@city.local")
                .passwordHash(passwordEncoder.encode("user123"))
                .fullName("Иван Иванов")
                .role(Role.USER)
                .build());

        userRepository.save(User.builder()
                .username("user2")
                .email("user2@city.local")
                .passwordHash(passwordEncoder.encode("user123"))
                .fullName("Пётр Петров")
                .role(Role.USER)
                .build());

        log.info("Созданы демо-пользователи: admin/admin123, user1/user123, user2/user123");
    }
}