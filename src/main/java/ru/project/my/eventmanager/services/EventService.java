package ru.project.my.eventmanager.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.my.eventmanager.converters.EventEntityConverter;
import ru.project.my.eventmanager.exceptions.ConditionUnacceptableException;
import ru.project.my.eventmanager.repositories.EventRepository;
import ru.project.my.eventmanager.repositories.LocationRepository;
import ru.project.my.eventmanager.repositories.RegistrationRepository;
import ru.project.my.eventmanager.repositories.UserRepository;
import ru.project.my.eventmanager.repositories.entity.EventEntity;
import ru.project.my.eventmanager.repositories.entity.LocationEntity;
import ru.project.my.eventmanager.repositories.entity.RegistrationEntity;
import ru.project.my.eventmanager.repositories.entity.UserEntity;
import ru.project.my.eventmanager.security.AuthenticationService;
import ru.project.my.eventmanager.services.model.Event;
import ru.project.my.eventmanager.services.model.EventStatus;
import ru.project.my.eventmanager.services.model.Role;
import ru.project.my.eventmanager.services.model.SearchFilter;
import ru.project.my.eventmanager.services.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventEntityConverter converter;
    private final RegistrationRepository registrationRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public EventService(EventRepository eventRepository, EventEntityConverter converter, RegistrationRepository registrationRepository, LocationRepository locationRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.eventRepository = eventRepository;
        this.converter = converter;
        this.registrationRepository = registrationRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
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
            throw new ConditionUnacceptableException("Нельзя создать Мероприятие в указанной Локации, так как максимальная вместимость Локации меньше требуемой для Мероприятия. Измените id локации или параметр 'maxPlaces'");
        }

        Long currentUserId = authenticationService.getCurrentUser().getId();
        UserEntity userEntity = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ConditionUnacceptableException("Пользователь с userId=%s отсутствует в системе".formatted(currentUserId)));

        EventEntity eventEntity = converter.toEntity(event);
        eventEntity.setLocation(existsLocation);
        eventEntity.setOccupiedPlaces(0);
        eventEntity.setStatus(EventStatus.WAIT_START);
        eventEntity.setOwner(userEntity);

        eventEntity = eventRepository.save(eventEntity);

        return converter.toEvent(eventEntity);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        EventEntity currentEvent = eventRepository.findByIdAndLock(eventId)
                .orElseThrow(() -> new ConditionUnacceptableException("Мероприятие с eventId=%s отсутствует в системе".formatted(eventId)));

        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(currentEvent.getOwner().getId()) && !Role.ADMIN.equals(currentUser.getRole())) {
            throw new AccessDeniedException("Текущий пользователь не может отменить это Мероприятие, так как он не является его создателем и у него нет роли ADMIN");
        }

        if (!currentEvent.getStatus().equals(EventStatus.WAIT_START)) {
            throw new ConditionUnacceptableException("Нельзя отменить это Мероприятие, так как оно уже %s".formatted(currentEvent.getStatus().getName()));
        }

        registrationRepository.deleteByEventId(eventId);

        currentEvent.setStatus(EventStatus.CANCELLED);
    }

    public Event getEvent(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new ConditionUnacceptableException("Мероприятие с eventId=%s отсутствует в системе".formatted(eventId)));

        return converter.toEvent(eventEntity);
    }

    @Transactional
    public Event updateEvent(Event event, Long locationId) {
        EventEntity existsEvent = eventRepository.findByIdAndLock(event.getId())
                .orElseThrow(() -> new ConditionUnacceptableException("Мероприятие с eventId=%s отсутствует в системе".formatted(event.getId())));

        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(existsEvent.getOwner().getId()) && !Role.ADMIN.equals(currentUser.getRole())) {
            throw new AccessDeniedException("Текущий пользователь не может изменить это Мероприятие, так как он не является его создателем и у него нет роли ADMIN");
        }

        LocationEntity existsLocation = locationRepository.findById(locationId)
                .orElseThrow(() -> new ConditionUnacceptableException("Локация с locationId=%s отсутствует в системе".formatted(locationId)));

        LocalDateTime minEventDate = existsEvent.getDate().minusDays(1);
        if (minEventDate.isBefore(LocalDateTime.now())) {
            throw new ConditionUnacceptableException("Изменить Мероприятие можно не позднее, чем за сутки до его начала");
        }

        minEventDate = event.getDate().minusDays(1);
        if (minEventDate.isBefore(LocalDateTime.now())) {
            throw new ConditionUnacceptableException("Нельзя установить дату Мероприятия меньше, чем текущая дата + 1 день");
        }

        if (event.getMaxPlaces().compareTo(existsLocation.getCapacity()) > 0) {
            throw new ConditionUnacceptableException("Нельзя установить максимальное количество мест на Мероприятии на %s, так как максимальная вместимость Локации меньше требуемой. Измените id локации или параметр 'maxPlaces'".formatted(event.getMaxPlaces()));
        }

        if (event.getMaxPlaces() < existsEvent.getOccupiedPlaces()) {
            throw new ConditionUnacceptableException("Нельзя установить максимальное количество мест на Мероприятии на %s, так как это меньше, чем уже зарегистрированных участников".formatted(event.getMaxPlaces()));
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
        Long currentUserId = authenticationService.getCurrentUser().getId();
        List<EventEntity> eventsList = eventRepository.findByOwnerId(currentUserId);

        return converter.toEvent(eventsList);
    }

    @Transactional
    public void registerUserOnEvent(Long eventId) {
        User currentUser = authenticationService.getCurrentUser();
        boolean userRegisteredAlready = registrationRepository.existsByUserIdAndEventId(currentUser.getId(), eventId);
        if (userRegisteredAlready) {
            throw new ConditionUnacceptableException("Пользователь итак уже зарегистрирован на это Мероприятие");
        }

        EventEntity eventEntity = eventRepository.findByIdAndLock(eventId)
                .orElseThrow(() -> new ConditionUnacceptableException("Мероприятие с eventId=%s отсутствует в системе".formatted(eventId)));

        if (!eventEntity.getStatus().equals(EventStatus.WAIT_START)) {
            throw new ConditionUnacceptableException("Нельзя зарегистрироваться на это Мероприятие, так как оно уже %s".formatted(eventEntity.getStatus().getName()));
        }

        if (eventEntity.getOccupiedPlaces() >= eventEntity.getMaxPlaces()) {
            throw new ConditionUnacceptableException("Нельзя зарегистрироваться на это Мероприятие, так как все места уже заняты");
        }

        RegistrationEntity registrationEntity = new RegistrationEntity(userRepository.getReferenceById(currentUser.getId()), eventEntity);
        registrationRepository.save(registrationEntity);

        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() + 1);
    }

    @Transactional
    public void cancelRegisterUserOnEvent(Long eventId) {
        User currentUser = authenticationService.getCurrentUser();
        RegistrationEntity registrationEntity = registrationRepository.findByUserIdAndEventId(currentUser.getId(), eventId)
                .orElseThrow(() -> new ConditionUnacceptableException("Пользователь итак не зарегистрирован на это Мероприятие"));

        EventEntity eventEntity = eventRepository.findByIdAndLock(eventId)
                .orElseThrow(() -> new ConditionUnacceptableException("Мероприятие с eventId=%s отсутствует в системе".formatted(eventId)));

        if (!eventEntity.getStatus().equals(EventStatus.WAIT_START)) {
            throw new ConditionUnacceptableException("Нельзя отказаться от регистрации на это Мероприятие, так как оно уже %s".formatted(eventEntity.getStatus().getName()));
        }

        registrationRepository.delete(registrationEntity);

        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() - 1);
    }

    public List<Event> getMyRegistrationsOnEvents() {
        List<EventEntity> events = eventRepository.findByUserRegistration(authenticationService.getCurrentUser().getId());

        return converter.toEvent(events);
    }
}
