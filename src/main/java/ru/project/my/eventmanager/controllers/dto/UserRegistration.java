package ru.project.my.eventmanager.controllers.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegistration {
    @NotNull
    @Size(min = 1, max = 255, message = "Требуется указать Логин пользователя (не длиннее 256 символов)")
    private String login;
    @NotNull
    @Size(min = 1, max = 255, message = "Требуется указать Пароль пользователя (не длиннее 256 символов)")
    private String password;
    @NotNull
    @Min(value = 14, message = "Регистрироваться на мероприятия можно только с 14 лет")
    @Max(value = 150, message = "Ни один человек еще не прожил больше 150 лет. Укажите правильный возраст")
    private Integer age;

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

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
}
