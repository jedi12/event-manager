package ru.project.my.eventmanager.services;

import org.springframework.stereotype.Service;
import ru.project.my.eventmanager.converters.UserEntityConverter;
import ru.project.my.eventmanager.exceptions.ConditionUnacceptableException;
import ru.project.my.eventmanager.exceptions.NotFoundException;
import ru.project.my.eventmanager.repositories.UserRepository;
import ru.project.my.eventmanager.repositories.entity.UserEntity;
import ru.project.my.eventmanager.services.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityConverter converter;

    public UserService(UserRepository userRepository, UserEntityConverter converter) {
        this.userRepository = userRepository;
        this.converter = converter;
    }

    public List<User> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        return converter.toUser(userEntities);
    }

    public User createUser(User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new ConditionUnacceptableException("Пользователь с таким логином уже существует");
        }

        UserEntity userEntity = converter.toEntity(user);
        userEntity.setRole(UserEntity.ROLE_USER);
        userEntity = userRepository.save(userEntity);

        return converter.toUser(userEntity);
    }

    public User getUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId=%s отсутствует в системе".formatted(userId)));

        return converter.toUser(userEntity);
    }

    public String authenticateUser(String login, String password) {
        throw new RuntimeException("Реализация метода предполагается в следующем ДЗ");
    }
}
