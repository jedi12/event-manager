package ru.project.my.eventmanager.controllers.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.project.my.eventmanager.services.model.EventStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {
    private Long id;
    private String name;
    private Long ownerId;
    private Integer maxPlaces;
    private Integer occupiedPlaces;
    private LocalDateTime date;
    private Integer cost;
    private Integer duration;
    private Long locationId;
    private EventStatus status;

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

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }
    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }
    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
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

    public EventStatus getStatus() {
        return status;
    }
    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
