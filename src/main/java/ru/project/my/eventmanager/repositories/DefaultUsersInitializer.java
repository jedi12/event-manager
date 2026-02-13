package ru.project.my.eventmanager.repositories;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.project.my.eventmanager.repositories.entity.UserEntity;
import ru.project.my.eventmanager.services.model.Role;

@Component
public class DefaultUsersInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUsersInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        createUserIfNotExists("admin", "admin", 45, Role.ADMIN);
        createUserIfNotExists("user", "user", 18, Role.USER);
    }

    private void createUserIfNotExists(String login, String password, Integer age, Role role) {
        if (userRepository.existsByLoginIgnoreCase(login)) return;

        String passHash = passwordEncoder.encode(password);
        userRepository.save(new UserEntity(login, passHash, age, role));
    }
}
