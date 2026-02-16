package ru.project.my.eventmanager.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.my.eventmanager.converters.UserEntityConverter;
import ru.project.my.eventmanager.exceptions.ConditionUnacceptableException;
import ru.project.my.eventmanager.exceptions.NotFoundException;
import ru.project.my.eventmanager.repositories.UserRepository;
import ru.project.my.eventmanager.repositories.entity.UserEntity;
import ru.project.my.eventmanager.services.model.Role;
import ru.project.my.eventmanager.services.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityConverter converter;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserEntityConverter converter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        return converter.toUser(userEntities);
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByLoginIgnoreCase(user.getLogin())) {
            throw new ConditionUnacceptableException("Пользователь с таким логином уже существует");
        }

        UserEntity userEntity = converter.toEntity(user);
        userEntity.setPassHash(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(Role.USER);
        userEntity = userRepository.save(userEntity);

        return converter.toUser(userEntity);
    }

    public User getUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId=%s отсутствует в системе".formatted(userId)));

        return converter.toUser(userEntity);
    }

    public User findUserByLogin(String login) {
        UserEntity userEntity = userRepository.findUseByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с login=%s отсутствует в системе".formatted(login)));

        return converter.toUser(userEntity);
    }
}
