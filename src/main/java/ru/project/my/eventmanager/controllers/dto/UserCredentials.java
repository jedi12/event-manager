package ru.project.my.eventmanager.controllers.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCredentials {
    @NotNull
    @Size(min = 1, max = 255, message = "Требуется указать Логин пользователя (не длиннее 256 символов)")
    private String login;
    @NotNull
    @Size(min = 1, max = 255, message = "Требуется указать Пароль пользователя (не длиннее 256 символов)")
    private String password;

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
