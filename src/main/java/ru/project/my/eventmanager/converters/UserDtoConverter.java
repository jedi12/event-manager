package ru.project.my.eventmanager.converters;

import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.controllers.dto.UserDto;
import ru.project.my.eventmanager.controllers.dto.UserRegistration;
import ru.project.my.eventmanager.services.model.User;

@Component
public class UserDtoConverter {

    public User toUser(UserRegistration userRegistration) {
        if (userRegistration == null) return null;

        User user = new User();
        user.setLogin(userRegistration.getLogin());
        user.setPassword(userRegistration.getPassword());
        user.setAge(userRegistration.getAge());
        return user;
    }

    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setAge(user.getAge());
        userDto.setRole(user.getRole().name());
        return userDto;
    }
}
