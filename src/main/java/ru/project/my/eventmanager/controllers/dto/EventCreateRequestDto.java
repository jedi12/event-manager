package ru.project.my.eventmanager.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class EventCreateRequestDto {
    @NotNull
    @Size(min = 1, max = 255, message = "Требуется указать название Мероприятия (не длиннее 256 символов)")
    private String name;
    @NotNull
    @Min(value = 1, message = "Максимальное количество мест на мероприятии не может быть 0")
    @Max(value = 150000, message = "Максимальное количество мест на мероприятии не может быть больше 150000")
    private Integer maxPlaces;
    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime date;
    @NotNull
    @Min(value = 0, message = "Стоимость мероприятия не может быть меньше нуля")
    private Integer cost;
    @NotNull
    @Min(value = 30, message = "Длительность мероприятия не может быть меньше 30 минут")
    private Integer duration;
    @NotNull
    @Min(1)
    private Long locationId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }
    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getLocationId() {
        return locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
