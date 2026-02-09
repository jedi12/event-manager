package ru.project.my.eventmanager.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.project.my.eventmanager.controllers.dto.EventCreateRequestDto;
import ru.project.my.eventmanager.controllers.dto.EventDto;
import ru.project.my.eventmanager.controllers.dto.EventSearchRequestDto;
import ru.project.my.eventmanager.controllers.dto.EventUpdateRequestDto;
import ru.project.my.eventmanager.converters.EventDtoConverter;
import ru.project.my.eventmanager.services.EventService;
import ru.project.my.eventmanager.services.model.Event;

import java.util.List;

@RestController
public class EventController {
    private final EventService eventService;
    private final EventDtoConverter dtoConverter;

    public EventController(EventService eventService, EventDtoConverter dtoConverter) {
        this.eventService = eventService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping("/events")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventCreateRequestDto eventCreateRequestDto) {
        Event event = eventService.createEvent(dtoConverter.toEvent(eventCreateRequestDto, null), eventCreateRequestDto.getLocationId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(event));
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long eventId) {
        Event event = eventService.getEvent(eventId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(event));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long eventId, @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        Event event = eventService.updateEvent(dtoConverter.toEvent(eventUpdateRequestDto, eventId), eventUpdateRequestDto.getLocationId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(event));
    }

    @PostMapping("/events/search")
    public ResponseEntity<List<EventDto>> searchEvents(@Valid @RequestBody EventSearchRequestDto eventSearchRequestDto) {
        List<Event> events = eventService.searchEvents(dtoConverter.toSearchFilters(eventSearchRequestDto));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(events));
    }

    @GetMapping("/events/my")
    public ResponseEntity<List<EventDto>> getMyEvents() {
        List<Event> events = eventService.getMyEvents();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(events));
    }

    @PostMapping("/events/registrations/{eventId}")
    public ResponseEntity<Void> registerUserOnEvent(@PathVariable Long eventId) {
        eventService.registerUserOnEvent(eventId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/events/registrations/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegisterUserOnEvent(@PathVariable Long eventId) {
        eventService.cancelRegisterUserOnEvent(eventId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/events/registrations/my")
    public ResponseEntity<List<EventDto>> getMyRegistrationsOnEvents() {
        List<Event> events = eventService.getMyRegistrationsOnEvents();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(events));
    }
}
