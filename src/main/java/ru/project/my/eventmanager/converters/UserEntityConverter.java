package ru.project.my.eventmanager.converters;

import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.repositories.entity.UserEntity;
import ru.project.my.eventmanager.services.model.User;

import java.util.Collections;
import java.util.List;

@Component
public class UserEntityConverter {

    public User toUser(UserEntity userEntity) {
        if (userEntity == null) return null;

        User user = new User();
        user.setId(userEntity.getId());
        user.setLogin(userEntity.getLogin());
        user.setPassword(userEntity.getPassword());
        user.setAge(userEntity.getAge());
        user.setRole(userEntity.getRole());
        return user;
    }

    public List<User> toUser(List<UserEntity> userEntityList) {
        if (userEntityList == null) return Collections.emptyList();
        return userEntityList.stream().map(this::toUser).toList();
    }

    public UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setLogin(user.getLogin());
        userEntity.setPassword(user.getPassword());
        userEntity.setAge(user.getAge());
        userEntity.setRole(user.getRole());
        return userEntity;
    }

    public List<UserEntity> toEntity(List<User> userList) {
        if (userList == null) return Collections.emptyList();
        return userList.stream().map(this::toEntity).toList();
    }
}
