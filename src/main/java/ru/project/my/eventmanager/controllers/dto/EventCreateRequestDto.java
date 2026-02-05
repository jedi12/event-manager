package ru.project.my.eventmanager.controllers.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

public class EventCreateRequestDto extends EventUpdateRequestDto {
    @NotNull
    @Override
    public String getName() {
        return super.getName();
    }

    @NotNull
    @Override
    public Integer getMaxPlaces() {
        return super.getMaxPlaces();
    }

    @NotNull
    @Override
    public LocalDateTime getDate() {
        return super.getDate();
    }

    @NotNull
    @Override
    public Integer getCost() {
        return super.getCost();
    }

    @NotNull
    @Override
    public Integer getDuration() {
        return super.getDuration();
    }

    @NotNull
    @Override
    public Long getLocationId() {
        return super.getLocationId();
    }
}
