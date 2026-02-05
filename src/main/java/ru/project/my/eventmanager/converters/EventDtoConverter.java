package ru.project.my.eventmanager.converters;

import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.controllers.dto.EventDto;
import ru.project.my.eventmanager.controllers.dto.EventSearchRequestDto;
import ru.project.my.eventmanager.controllers.dto.EventUpdateRequestDto;
import ru.project.my.eventmanager.services.model.Event;
import ru.project.my.eventmanager.services.model.SearchFilter;

import java.util.Collections;
import java.util.List;

@Component
public class EventDtoConverter {

    public Event toEvent(EventUpdateRequestDto eventUpdateRequestDto, Long eventId) {
        if (eventUpdateRequestDto == null) return null;

        Event event = new Event();
        event.setId(eventId);
        event.setName(eventUpdateRequestDto.getName());
        event.setMaxPlaces(eventUpdateRequestDto.getMaxPlaces());
        event.setDate(eventUpdateRequestDto.getDate());
        event.setCost(eventUpdateRequestDto.getCost());
        event.setDuration(eventUpdateRequestDto.getDuration());
        return event;
    }

    public EventDto toDto(Event event) {
        if (event == null) return null;

        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setOwnerId(event.getOwner() == null ? null : event.getOwner().getId());
        eventDto.setMaxPlaces(event.getMaxPlaces());
        eventDto.setOccupiedPlaces(event.getOccupiedPlaces());
        eventDto.setDate(event.getDate());
        eventDto.setCost(event.getCost());
        eventDto.setDuration(event.getDuration());
        eventDto.setLocationId(event.getLocation() == null ? null :event.getLocation().getId());
        eventDto.setStatus(event.getStatus());
        return eventDto;
    }

    public List<EventDto> toDto(List<Event> events) {
        if (events == null) return Collections.emptyList();
        return events.stream().map(this::toDto).toList();
    }

    public SearchFilter toSearchFilters(EventSearchRequestDto eventSearchRequestDto) {
        if (eventSearchRequestDto == null) return null;

        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setName(eventSearchRequestDto.getName());
        searchFilter.setPlacesMin(eventSearchRequestDto.getPlacesMin());
        searchFilter.setPlacesMax(eventSearchRequestDto.getPlacesMax());
        searchFilter.setDateStartAfter(eventSearchRequestDto.getDateStartAfter());
        searchFilter.setDateStartBefore(eventSearchRequestDto.getDateStartBefore());
        searchFilter.setCostMin(eventSearchRequestDto.getCostMin());
        searchFilter.setCostMax(eventSearchRequestDto.getCostMax());
        searchFilter.setDurationMin(eventSearchRequestDto.getDurationMin());
        searchFilter.setDurationMax(eventSearchRequestDto.getDurationMax());
        searchFilter.setLocationId(eventSearchRequestDto.getLocationId());
        searchFilter.setEventStatus(eventSearchRequestDto.getEventStatus());
        return searchFilter;
    }
}
