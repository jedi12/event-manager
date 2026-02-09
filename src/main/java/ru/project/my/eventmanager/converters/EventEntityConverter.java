package ru.project.my.eventmanager.converters;

import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.repositories.entity.EventEntity;
import ru.project.my.eventmanager.services.model.Event;

import java.util.Collections;
import java.util.List;

@Component
public class EventEntityConverter {
    private final UserEntityConverter userEntityConverter;
    private final LocationEntityConverter locationEntityConverter;

    public EventEntityConverter(UserEntityConverter userEntityConverter, LocationEntityConverter locationEntityConverter) {
        this.userEntityConverter = userEntityConverter;
        this.locationEntityConverter = locationEntityConverter;
    }

    public Event toEvent(EventEntity eventEntity) {
        if (eventEntity == null) return null;

        Event event = new Event();
        event.setId(eventEntity.getId());
        event.setName(eventEntity.getName());
        event.setOwner(userEntityConverter.toUser(eventEntity.getOwner()));
        event.setMaxPlaces(eventEntity.getMaxPlaces());
        event.setOccupiedPlaces(eventEntity.getOccupiedPlaces());
        event.setDate(eventEntity.getDate());
        event.setCost(eventEntity.getCost());
        event.setDuration(eventEntity.getDuration());
        event.setLocation(locationEntityConverter.toLocation(eventEntity.getLocation()));
        event.setStatus(eventEntity.getStatus());
        return event;
    }

    public List<Event> toEvent(List<EventEntity> eventEntityList) {
        if (eventEntityList == null) return Collections.emptyList();
        return eventEntityList.stream().map(this::toEvent).toList();
    }

    public EventEntity toEntity(Event event) {
        if (event == null) return null;

        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(event.getId());
        eventEntity.setName(event.getName());
        eventEntity.setOwner(userEntityConverter.toEntity(event.getOwner()));
        eventEntity.setMaxPlaces(event.getMaxPlaces());
        eventEntity.setOccupiedPlaces(event.getOccupiedPlaces());
        eventEntity.setDate(event.getDate());
        eventEntity.setCost(event.getCost());
        eventEntity.setDuration(event.getDuration());
        eventEntity.setLocation(locationEntityConverter.toEntity(event.getLocation()));
        eventEntity.setStatus(event.getStatus());
        return eventEntity;
    }

    public List<EventEntity> toEntity(List<Event> eventList) {
        if (eventList == null) return Collections.emptyList();
        return eventList.stream().map(this::toEntity).toList();
    }
}
