package ru.project.my.eventmanager.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.time.LocalDateTime;

public class EventSearchRequestDto {
    @Size(min = 1, max = 255, message = "Название Мероприятия не может быть меньше 1 символа и больше 256 символов)")
    private String name;
    @Min(value = 1, message = "Количество мест на мероприятии не может быть меньше 1")
    @Max(value = 150000, message = "Количество мест на мероприятии не может быть больше 150000")
    private Integer placesMin;
    @Min(value = 1, message = "Количество мест на мероприятии не может быть меньше 1")
    @Max(value = 150000, message = "Количество мест на мероприятии не может быть больше 150000")
    private Integer placesMax;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime dateStartAfter;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime dateStartBefore;
    @Min(value = 0, message = "Стоимость мероприятия не может быть меньше нуля")
    private Integer costMin;
    @Min(value = 0, message = "Стоимость мероприятия не может быть меньше нуля")
    private Integer costMax;
    @Min(value = 30, message = "Длительность мероприятия не может быть меньше 30 минут")
    private Integer durationMin;
    @Min(value = 30, message = "Длительность мероприятия не может быть меньше 30 минут")
    private Integer durationMax;
    @Min(1)
    private Long locationId;
    private EventStatus eventStatus;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlacesMin() {
        return placesMin;
    }
    public void setPlacesMin(Integer placesMin) {
        this.placesMin = placesMin;
    }

    public Integer getPlacesMax() {
        return placesMax;
    }
    public void setPlacesMax(Integer placesMax) {
        this.placesMax = placesMax;
    }

    public LocalDateTime getDateStartAfter() {
        return dateStartAfter;
    }
    public void setDateStartAfter(LocalDateTime dateStartAfter) {
        this.dateStartAfter = dateStartAfter;
    }

    public LocalDateTime getDateStartBefore() {
        return dateStartBefore;
    }
    public void setDateStartBefore(LocalDateTime dateStartBefore) {
        this.dateStartBefore = dateStartBefore;
    }

    public Integer getCostMin() {
        return costMin;
    }
    public void setCostMin(Integer costMin) {
        this.costMin = costMin;
    }

    public Integer getCostMax() {
        return costMax;
    }
    public void setCostMax(Integer costMax) {
        this.costMax = costMax;
    }

    public Integer getDurationMin() {
        return durationMin;
    }
    public void setDurationMin(Integer durationMin) {
        this.durationMin = durationMin;
    }

    public Integer getDurationMax() {
        return durationMax;
    }
    public void setDurationMax(Integer durationMax) {
        this.durationMax = durationMax;
    }

    public Long getLocationId() {
        return locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }
    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
