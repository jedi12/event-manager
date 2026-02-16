package ru.project.my.eventmanager.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.project.my.eventmanager.controllers.dto.JwtResponse;
import ru.project.my.eventmanager.controllers.dto.UserCredentials;
import ru.project.my.eventmanager.controllers.dto.UserDto;
import ru.project.my.eventmanager.controllers.dto.UserRegistration;
import ru.project.my.eventmanager.converters.UserDtoConverter;
import ru.project.my.eventmanager.security.AuthenticationService;
import ru.project.my.eventmanager.services.UserService;
import ru.project.my.eventmanager.services.model.User;

@RestController
public class UserController {
    private final UserService userService;
    private final UserDtoConverter dtoConverter;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, UserDtoConverter dtoConverter, AuthenticationService authenticationService) {
        this.userService = userService;
        this.dtoConverter = dtoConverter;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegistration userDto) {
        User user = userService.createUser(dtoConverter.toUser(userDto));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(user));
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable Long userId) {
        User user = userService.getUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(user));
    }

    @PostMapping("/users/auth")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserCredentials userCredentials) {
        String jwtToken = authenticationService.authenticateUser(userCredentials.getLogin(), userCredentials.getPassword());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse(jwtToken));
    }
}
