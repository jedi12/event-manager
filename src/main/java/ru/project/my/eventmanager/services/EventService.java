package ru.project.my.eventmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.my.eventmanager.converters.EventEntityConverter;
import ru.project.my.eventmanager.exceptions.ConditionUnacceptableException;
import ru.project.my.eventmanager.repositories.EventRepository;
import ru.project.my.eventmanager.repositories.LocationRepository;
import ru.project.my.eventmanager.repositories.entity.EventEntity;
import ru.project.my.eventmanager.repositories.entity.LocationEntity;
import ru.project.my.eventmanager.services.model.Event;
import ru.project.my.eventmanager.services.model.EventStatus;
import ru.project.my.eventmanager.services.model.SearchFilter;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventEntityConverter converter;
    private final LocationRepository locationRepository;

    public EventService(EventRepository eventRepository, EventEntityConverter converter, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.converter = converter;
        this.locationRepository = locationRepository;
    }

    public List<Event> getAllEvents() {
        List<EventEntity> locationEntity = eventRepository.findAll();

        return converter.toEvent(locationEntity);
    }

    @Transactional
    public Event createEvent(Event event, Long locationId) {
        LocationEntity existsLocation = locationRepository.findById(locationId)
                .orElseThrow(() -> new ConditionUnacceptableException("Локация с locationId=%s отсутствует в системе".formatted(locationId)));

        LocalDateTime minEventDate = event.getDate().minusDays(1);
        if (minEventDate.isBefore(LocalDateTime.now())) {
            throw new ConditionUnacceptableException("Создать Мероприятие можно не позднее, чем за сутки до его начала. Измените значение 'date'");
        }

        if (event.getMaxPlaces().compareTo(existsLocation.getCapacity()) > 0) {
            throw new ConditionUnacceptableException("Нельзя создать Мероприятие в указанной Локации, так как максимальная вместимость Локации меньше требуемой в Мероприятии. Измените id локации или параметр 'maxPlaces'");
        }

        EventEntity eventEntity = converter.toEntity(event);
        eventEntity.setLocation(existsLocation);
        eventEntity.setOccupiedPlaces(0);
        eventEntity.setStatus(EventStatus.WAIT_START);
        // Работа с пользователем будет реализована в следующем ДЗ
//        eventEntity.setOwner(currentUser);

        eventEntity = eventRepository.save(eventEntity);

        return converter.toEvent(eventEntity);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        EventEntity currentEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ConditionUnacceptableException("Мероприятие с eventId=%s отсутствует в системе".formatted(eventId)));

        if (!currentEvent.getStatus().equals(EventStatus.WAIT_START)) {
            throw new ConditionUnacceptableException("Нельзя отменить это Мероприятие, так как оно уже %s".formatted(currentEvent.getStatus().getName()));
        }

        currentEvent.setStatus(EventStatus.CANCELLED);
        eventRepository.save(currentEvent);
    }

    public Event getEvent(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new ConditionUnacceptableException("Мероприятие с eventId=%s отсутствует в системе".formatted(eventId)));

        return converter.toEvent(eventEntity);
    }

    @Transactional
    public Event updateEvent(Event event, Long locationId) {
        EventEntity existsEvent = eventRepository.findById(event.getId())
                .orElseThrow(() -> new ConditionUnacceptableException("Мероприятие с eventId=%s отсутствует в системе".formatted(event.getId())));

        LocationEntity existsLocation = locationRepository.findById(locationId)
                .orElseThrow(() -> new ConditionUnacceptableException("Локация с locationId=%s отсутствует в системе".formatted(locationId)));

        LocalDateTime minEventDate = existsEvent.getDate().minusDays(1);
        if (minEventDate.isBefore(LocalDateTime.now())) {
            throw new ConditionUnacceptableException("Изменить Мероприятие можно не позднее, чем за сутки до его начала");
        }

        if (event.getDate().isBefore(LocalDateTime.now())) {
            throw new ConditionUnacceptableException("Нельзя установить дату Мероприятия меньше, чем текущая дата + 1 день");
        }

        if (event.getMaxPlaces().compareTo(existsLocation.getCapacity()) > 0) {
            throw new ConditionUnacceptableException("Нельзя изменить Мероприятие в указанной Локации, так как максимальная вместимость Локации меньше требуемой в Мероприятии. Измените id локации или параметр 'maxPlaces'");
        }

        existsEvent.setName(event.getName());
        existsEvent.setMaxPlaces(event.getMaxPlaces());
        existsEvent.setDate(event.getDate());
        existsEvent.setCost(event.getCost());
        existsEvent.setDuration(event.getDuration());
        existsEvent.setLocation(existsLocation);

        existsEvent = eventRepository.save(existsEvent);

        return converter.toEvent(existsEvent);
    }

    public List<Event> searchEvents(SearchFilter searchFilter) {
        List<EventEntity> eventsList = eventRepository.searchBySearchFilter(
                searchFilter.getName(),
                searchFilter.getOwnerId(),
                searchFilter.getPlacesMin(),
                searchFilter.getPlacesMax(),
                searchFilter.getDateStartAfter(),
                searchFilter.getDateStartBefore(),
                searchFilter.getCostMin(),
                searchFilter.getCostMax(),
                searchFilter.getDurationMin(),
                searchFilter.getDurationMax(),
                searchFilter.getLocationId(),
                searchFilter.getEventStatus()
        );

        return converter.toEvent(eventsList);
    }

    public List<Event> getMyEvents() {
        throw new RuntimeException("Реализация метода предполагается в следующем ДЗ");
    }

    public void registerUserOnEvent(Long eventId) {
        throw new RuntimeException("Реализация метода предполагается в следующем ДЗ");
    }

    public void cancelRegisterUserOnEvent(Long eventId) {
        throw new RuntimeException("Реализация метода предполагается в следующем ДЗ");
    }

    public List<Event> getMyRegistrationsOnEvents() {
        throw new RuntimeException("Реализация метода предполагается в следующем ДЗ");
    }
}
