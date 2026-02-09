package ru.project.my.eventmanager.controllers.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationDto {
    @Null(message = "Не надо указывать id Локации при создании. Вы его не знаете")
    private Long id;
    @NotNull
    @Size(min = 1, max = 256, message = "Требуется указать название Локации (не длиннее 256 символов)")
    private String name;
    @NotNull
    @Size(min = 1, max = 256, message = "Требуется указать адрес Локации (не длиннее 256 символов)")
    private String address;
    @NotNull
    @Min(value = 5, message = "Минимальная вместительность Локации: 5 человек")
    @Max(value = 150000, message = "Максимальная вместительность Локации: 150000 человек")
    private Integer capacity;
    @Size(max = 256, message = "Описание локации не должно превышать 256 символов")
    private String description;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
